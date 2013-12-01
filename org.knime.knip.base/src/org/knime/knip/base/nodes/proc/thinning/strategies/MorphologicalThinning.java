/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * Created on 01.12.2013 by Andreas
 */
package org.knime.knip.base.nodes.proc.thinning.strategies;

import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.type.logic.BitType;

/**
 *
 * @author Andreas
 */
public class MorphologicalThinning extends Abstract3x3NeighbourhoodThinning {

    private int iteration = 0;

    public MorphologicalThinning(final boolean foreground)
    {
        super(foreground);
    }

    @Override
    public boolean removePixel(final long[] position, final RandomAccessible<BitType> img) {

        RandomAccess<BitType> access = img.randomAccess();
        access.setPosition(position);

        boolean[] vals = getNeighbourhood(access);

        if(iteration % 4 == 0) {
            return top(vals);
        }

        if(iteration % 4 == 1) {
            return right(vals);
        }

        if(iteration % 4 == 2) {
            return bottom(vals);
        }

        if(iteration % 4 == 3) {
            return left(vals);
        }

        return false;

    }


    private boolean top(final boolean[] vals) {
        if (vals[1] == m_Background && vals[2] == m_Background && vals[8] == m_Background && vals[4] == m_Foreground
                && vals[5] == m_Foreground && vals[6] == m_Foreground) {
            return true;
        }
        if (vals[1] == m_Background && vals[2] == m_Background && vals[3] == m_Background && vals[5] == m_Foreground
                && vals[7] == m_Foreground) {
            return true;
        }

        return false;
    }

    private boolean right(final boolean[] vals) {
        if (vals[2] == m_Background && vals[3] == m_Background && vals[4] == m_Background && vals[6] == m_Foreground
                && vals[7] == m_Foreground && vals[8] == m_Foreground) {
            return true;
        }
        if (vals[3] == m_Background && vals[4] == m_Background && vals[5] == m_Background && vals[1] == m_Foreground
                && vals[7] == m_Foreground) {
            return true;
        }

        return false;
    }

    private boolean bottom(final boolean[] vals) {
        if (vals[4] == m_Background && vals[5] == m_Background && vals[6] == m_Background && vals[1] == m_Foreground
                && vals[2] == m_Foreground && vals[8] == m_Foreground) {
            return true;
        }
        if (vals[5] == m_Background && vals[6] == m_Background && vals[7] == m_Background && vals[1] == m_Foreground
                && vals[3] == m_Foreground) {
            return true;
        }

        return false;
    }

    private boolean left(final boolean[] vals) {
        if (vals[8] == m_Background && vals[7] == m_Background && vals[6] == m_Background && vals[2] == m_Foreground
                && vals[3] == m_Foreground && vals[4] == m_Foreground) {
            return true;
        }
        if (vals[7] == m_Background && vals[8] == m_Background && vals[1] == m_Background && vals[5] == m_Foreground
                && vals[3] == m_Foreground) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterIteration() {
        ++iteration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getIterationsPerCycle() {
        return 4;
    }

}
