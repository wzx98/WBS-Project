package de.fhbingen.wbs.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Created by Maxi on 20.01.14.
 */
public class StaticUtilityMethods {
    /**
     * Gridbaglayout helper method.
     * @param container container that component gets added on.
     * @param component component to be added and set constraints for.
     * @param gridx GridBagConstraints argument.
     * @param gridy GridBagConstraints argument.
     * @param gridwidth GridBagConstraints argument.
     * @param gridheight GridBagConstraints argument.
     * @param weightx GridBagConstraints argument.
     * @param weighty GridBagConstraints argument.
     * @param anchor GridBagConstraints argument.
     * @param fill GridBagConstraints argument.
     * @param insets GridBagConstraints argument.
     * @param ipadx GridBagConstraints argument.
     * @param ipady GridBagConstraints argument.
     */
    public static void addWithAllGridBagConstraints(final Container container,
                                                    final Component component,
                                                    final int gridx,
                                                    final int gridy,
                                                    final int gridwidth,
                                                    final int gridheight,
                                                    final double weightx,
                                                    final double weighty,
                                                    final int anchor,
                                                    final int fill,
                                                    final Insets insets,
                                                    final int ipadx,
                                                    final int ipady) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx        = gridx;
        gridBagConstraints.gridy        = gridy;
        gridBagConstraints.gridheight   = gridheight;
        gridBagConstraints.gridwidth    = gridwidth;
        gridBagConstraints.weightx      = weightx;
        gridBagConstraints.weighty      = weighty;
        gridBagConstraints.anchor       = anchor;
        gridBagConstraints.fill         = fill;
        gridBagConstraints.insets       = insets;
        gridBagConstraints.ipadx        = ipadx;
        gridBagConstraints.ipady        = ipady;
        container.add(component, gridBagConstraints);
    }

    /**
     * Helper method that taked a component and adds it to a container using
     * {@link java.awt.GridBagConstraints}.
     * @param container The container to add to.
     * @param component The componend to be added.
     * @param gridx GridBagConstraints value.
     * @param gridy GridBagConstraints value.
     * @param weightx GridBagConstraints value.
     * @param weighty GridBagConstraints value.
     * @param fill GridBagConstraints value.
     * @param insets GridBagConstraints value.
     */
    public static void addWithGridBagConstraints(final Container container,
                                                 final Component component,
                                                 final int gridx,
                                                 final int gridy,
                                                 final double weightx,
                                                 final double weighty,
                                                 final int fill,
                                                 final Insets insets) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx        = gridx;
        gridBagConstraints.gridy        = gridy;
        gridBagConstraints.weightx      = weightx;
        gridBagConstraints.weighty      = weighty;
        gridBagConstraints.fill         = fill;
        gridBagConstraints.insets       = insets;
        container.add(component, gridBagConstraints);
    }

    /**
     * Helper method that taked a component and adds it to a container using
     * {@link java.awt.GridBagConstraints}.
     * @param container The container to add to.
     * @param component The componend to be added.
     * @param gridx GridBagConstraints value.
     * @param gridy GridBagConstraints value.
     * @param fill GridBagConstraints value.
     */
    public static void addWithLesserGridBagConstraints(final Container
                                                              container,
                                                 final Component component,
                                                 final int gridx,
                                                 final int gridy,
                                                 final int fill) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx        = gridx;
        gridBagConstraints.gridy        = gridy;
        gridBagConstraints.fill         = fill;
        container.add(component, gridBagConstraints);
    }
}
