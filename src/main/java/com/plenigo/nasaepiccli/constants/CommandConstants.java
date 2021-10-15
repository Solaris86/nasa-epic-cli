package com.plenigo.nasaepiccli.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandConstants {

    public final String SHELL_OPTION_DATE_HELP_MESSAGE = "Date when the images were taken. If empty most recent date will be used, otherwise date must be in format yyyy-MM-dd e.g. 2021-10-12";
    public final String SHELL_OPTION_FOLDER_COLOR_MESSAGE = "Color of the images [natural|enhanced]. Default value is [natural]";
    public final String SHELL_OPTION_FOLDER_HELP_MESSAGE = "Folder where the images will be saved";

    public final String IMAGE_COLOR_NATURAL = "natural";
    public final String IMAGE_COLOR_ENHANCED = "enhanced";

}
