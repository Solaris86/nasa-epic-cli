package com.plenigo.nasaepiccli.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandConstants {

    public final String SHELL_COMMAND_SAVE = "save";

    public final String SHELL_OPTION_DATE_HELP_MESSAGE = "Date when the images were taken. If empty most recent date will be used, otherwise date must be in format yyyy-MM-dd e.g. 2021-10-12";
    public final String SHELL_OPTION_COLOR_MESSAGE = "Color of the images [natural|enhanced]. Default value is [natural]";
    public final String SHELL_OPTION_PATH_HELP_MESSAGE = "Path on the hard drive where the images will be saved";

    public final String IMAGE_COLOR_NATURAL = "natural";
    public final String IMAGE_COLOR_ENHANCED = "enhanced";

}
