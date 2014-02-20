package de.fhbingen.wbs.dbServices;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.wpConflict.ConflictController;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klasse dient als Verbindung zur Datenbank fuer das Speicher und
 * Loeschen von Konflikten.<br/>
 *
 * @author Jens Eckes, Sven Seckler
 * @version 2.0 - 2012-08-20
 */
public class ConflictService {

    /**
     * s Liefert ein Set mit allen Konflikten des Projekts
     *
     * @return Set<Conflict> alle Konflikte
     */
    public static Set<ConflictController> getAllConflicts() {
        try {
            return fillConflicts(new HashSet<ConflictController>(), DBModelManager
                    .getConflictsModel().getConflicts());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Speichert einen aufgetreten Konflikt
     *
     * @param conflict
     *            Conflict der zu speichernde Konflikt
     * @throws SQLException
     */
    public static boolean setConflict(ConflictController conflict) {
        String trigger = conflict.getTriggerApStringId();
        if (trigger == null || (trigger != null && trigger.equals(""))) {
            trigger = WpManager.getRootAp().getStringID();
        }
        de.fhbingen.wbs.dbaccess.data.Conflict conf = new de.fhbingen.wbs.dbaccess.data.Conflict();
        conf.setFid_wp(conflict.getTriggerAp());
        conf.setFid_wp_affected(conflict.getAffectedAP());
        conf.setFid_emp(conflict.getUserId());
        conf.setReason(conflict.getReason());
        conf.setOccurence_date(conflict.getDate());
        return DBModelManager.getConflictsModel().addNewConflict(conf);

    }

    /**
     * Loescht einen behobenen Konflikt
     *
     * @param conflict
     *            Conflict der zu loeschende Konflikt
     * @throws SQLException
     */
    public static void deleteConflict(ConflictController conflict) throws SQLException {
        DBModelManager.getConflictsModel().deleteConflict(conflict.getId());
    }

    /**
     * Loescht alle Konflikte
     *
     * @throws SQLException
     */
    public static void deleteAll() {
        DBModelManager.getConflictsModel().deleteConflicts();
    }

    /**
     * Private Methode zum fuellen eines Sets von Conflicts mit den Daten eines
     * ResultSets
     *
     * @param conSet
     *            das zu fuellende Set vom Typ Conflict
     * @param resSet
     *            das ResultSet mit den Daten
     * @return das gefuellte Set
     * @throws SQLException
     */
    private static Set<ConflictController> fillConflicts(Set<ConflictController> conSet,
            final List<de.fhbingen.wbs.dbaccess.data.Conflict> conflicts) throws SQLException {
        for (de.fhbingen.wbs.dbaccess.data.Conflict conf : conflicts) {
            conSet.add(new ConflictController(conf.getOccurence_date(), conf.getReason(),
                    conf.getFid_emp(), conf.getFid_wp(), conf
                            .getFid_wp_affected()));
        }
        return conSet;
    }
}
