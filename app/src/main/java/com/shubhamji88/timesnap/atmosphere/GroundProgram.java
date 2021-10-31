

package com.shubhamji88.timesnap.atmosphere;

import android.content.res.Resources;


import com.shubhamji88.timesnap.R;

import gov.nasa.worldwind.util.Logger;
import gov.nasa.worldwind.util.WWUtil;

public class GroundProgram extends AtmosphereProgram {

    public static final Object KEY = GroundProgram.class;

    public GroundProgram(Resources resources) {
        try {
            String vs = WWUtil.readResourceAsText(resources, R.raw.gov_nasa_worldwind_groundprogram_vert);
            String fs = WWUtil.readResourceAsText(resources, R.raw.gov_nasa_worldwind_groundprogram_frag);
            this.setProgramSources(vs, fs);
            this.setAttribBindings("vertexPoint", "vertexTexCoord");
        } catch (Exception logged) {
            Logger.logMessage(Logger.ERROR, "GroundProgram", "constructor", "errorReadingProgramSource", logged);
        }
    }
}
