package de.fhbingen.wbs.wpConflict;

import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.fhbingen.wbs.dbServices.ConflictService;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpOverview.WPOverviewGUI;
import de.fhbingen.wbs.wpShow.WPShow;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * GUI-Klasse, die die Konflikte anzeigt<br />
 * und Methoden bereitstellt neue hinzuzufuegen und alle zu loeschen<br/>
 *
 * @author Michael Anstatt, Marc-Eric Baumgärtner, Sven Seckler
 * @version 2.0 - 20.08.2012
 */
public class ConflictTable extends JTable {
    /**
     * Serialization UID.
     */
    private static final long serialVersionUID = -6055310228750206338L;

    /**
     * Date Format.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy HH:mm");

    /**
     * Default table model.
     */
    private DefaultTableModel model;

    /**
     * All conflicts.
     */
    private ArrayList<Conflict> conflicts;

    /**
     * The context menu.
     */
    private JPopupMenu contextMenu;

    /**
     * Number of the last clicked row.
     */
    private int lastClickedRow;

    /**
     * WPOverview instance.
     */
    private WPOverview over;


    /**
     * Translation interface.
     */
    private final General generalStrings;

    /**
     * Constructor.
     *
     * @param wpOverview
     *            WPOverview GUI.
     * @param parent
     *            ParentFrame.
     */
    public ConflictTable(final WPOverview wpOverview, final JFrame parent) {
        generalStrings = LocalizedStrings.getGeneralStrings();
        reload();
        this.over = wpOverview;
        contextMenu = new JPopupMenu();
        JMenuItem miRemove = new JMenuItem(LocalizedStrings.getButton()
                .delete(LocalizedStrings.getGeneralStrings().conflict()));
        miRemove.setIcon(WPOverviewGUI.delAP);
        miRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { //TODO MVC that.
                removeConflict(lastClickedRow);
            }

        });
        contextMenu.add(miRemove);

        final JTable thisTable = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lastClickedRow = thisTable.rowAtPoint(e.getPoint());
                if (e.getButton() == MouseEvent.BUTTON3) {
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());

                    thisTable.setEditingRow(lastClickedRow);
                } else if (e.getClickCount() == 2) {
                    if (conflicts.get(thisTable.rowAtPoint(e.getPoint()))
                            .getTriggerApStringId() != null) {
                        Workpackage selected =
                                WpManager.getWorkpackage(conflicts.get(
                                        thisTable.rowAtPoint(e.getPoint()))
                                        .getTriggerApStringId());
                        new WPShow(wpOverview, selected, false, parent);
                    }

                }
            }
        });
    }

    /**
     * Deleted conflict from UI and Database.
     * TODO fix conflict deletion.
     * @param row
     *          Number of row of the conflict.
     */
    private void removeConflict(final int row) {
        try {
            ConflictService.deleteConflict(conflicts.remove(row));
            model.removeRow(row);
            over.reload();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a conflict to the UI and the database.
     *
     * @param conflict
     *            Conflict to be added.
     */
    public void addConflict(final Conflict conflict) {
        Set<Conflict> conflicts = new HashSet<Conflict>();
        conflicts.add(conflict);
        addConflicts(conflicts);
    }

    /**
     * Fuegt einen Set mit Konflikt der DB und der GUI hinzu
     *
     * @param newConflicts
     *            Set von CKnflikten die hinzugefuegt werden sollen
     */
    public void addConflicts(Set<Conflict> newConflicts) {
        for (int i = 0; i < this.getRowCount(); i++) {
            model.removeRow(i);
        }
        HashSet<Conflict> singleConflicts = new HashSet<Conflict>(conflicts);
        for (Conflict actualConflict : newConflicts) {
            if (!singleConflicts.contains(actualConflict)) {
                conflicts.add(actualConflict);
                model.addRow(createStringArray(actualConflict));
                ConflictService.setConflict(actualConflict);
            }
        }
        this.repaint();
    }

    /**
     * Liefert einen String Array mit allen Daten des uebergebenen Konflikts
     *
     * @param c
     * @return
     */
    private String[] createStringArray(Conflict c) {
        return new String[] {
                DATE_FORMAT.format(c.getDate()),
                createReasonString(c),
                DBModelManager.getEmployeesModel().getEmployee(c.getUserId())
                        .getLogin(), createAffectedString(c) };
    }

    /**
     * Liefert eine Beschreibung des Konflikts
     *
     * @param conflict
     *            deren Beschreibung man will
     * @return
     */
    private String createReasonString(Conflict conflict) {
        return conflict.getReasonString();
    }

    /**
     * Liefert einen String mit der ID des betroffenen APs
     *
     * @param conflict
     *            deren betroffene APs man will
     * @return
     */
    private String createAffectedString(Conflict conflict) {
        String affectedAPs = "";
        if (conflict.getAffectedApStringId() == null) {
            affectedAPs = conflict.getTriggerApStringId();
        } else {
            affectedAPs =
                    conflict.getTriggerApStringId() + "; " + conflict.getAffectedApStringId();
        }

        if (affectedAPs != null && affectedAPs.length() != 0)
            return affectedAPs;
        else
            return "";

    }

    /**
     * Laedt die Konflikttabelle neu
     */
    public void reload() {
        model = new DefaultTableModel();

        model.addColumn(generalStrings.pointOfTime());
        model.addColumn(generalStrings.reason());
        model.addColumn(generalStrings.causer());
        model.addColumn(LocalizedStrings.getWbs().affectedWorkpackages());

        conflicts = new ArrayList<Conflict>(ConflictService.getAllConflicts());

        for (Conflict actualConflict : conflicts) {
            model.addRow(createStringArray(actualConflict));
        }

        this.setModel(model);
    }

    /**
     * Sperrt alle Felder der Tabelle fuer Aenderungen
     */
    @Override
    public boolean isCellEditable(int x, int y) {
        return false;
    }
}
