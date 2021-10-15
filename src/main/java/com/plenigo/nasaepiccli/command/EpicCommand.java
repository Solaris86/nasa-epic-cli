package com.plenigo.nasaepiccli.command;

import com.plenigo.nasaepiccli.constants.CommandConstants;
import com.plenigo.nasaepiccli.dto.Image;
import com.plenigo.nasaepiccli.service.NasaEpicService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class EpicCommand {

    private final NasaEpicService nasaEpicService;
    private final MessageSource messageSource;

    @ShellMethod(key = "fetch", value = "Fetches images from NASA EPIC API and saves them in a provided folder")
    public String fetchEpicImages(@ShellOption(value = {"-D", "--date"}, defaultValue = "", help = CommandConstants.SHELL_OPTION_DATE_HELP_MESSAGE) String date,
                                  @ShellOption(value = {"-C", "--color"}, defaultValue = CommandConstants.IMAGE_COLOR_NATURAL, help = CommandConstants.SHELL_OPTION_FOLDER_COLOR_MESSAGE) String color,
                                  @ShellOption(value = {"-F", "--folder"}, help = CommandConstants.SHELL_OPTION_FOLDER_HELP_MESSAGE) String folderName) {

        List<Image> images = nasaEpicService.fetchImageMetadata(date, color);

        return "Fetched " + images.size() + " images";
    }

}
