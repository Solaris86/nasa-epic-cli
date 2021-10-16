package com.plenigo.nasaepiccli.command;

import com.plenigo.nasaepiccli.constants.CommandConstants;
import com.plenigo.nasaepiccli.model.ImageList;
import com.plenigo.nasaepiccli.service.ImagePersistService;
import com.plenigo.nasaepiccli.service.NasaEpicService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Files;
import java.nio.file.Paths;

@ShellComponent
@RequiredArgsConstructor
public class EpicCommand {

    private static final Logger logger = LoggerFactory.getLogger(EpicCommand.class);

    private final NasaEpicService nasaEpicService;
    private final ImagePersistService imagePersistService;

    @ShellMethod(key = CommandConstants.SHELL_COMMAND_SAVE, value = "Fetches images from NASA EPIC API and saves them in a provided folder")
    public String saveEpicImages(@ShellOption(value = {"-D", "--date"}, defaultValue = "", help = CommandConstants.SHELL_OPTION_DATE_HELP_MESSAGE) String date,
                                  @ShellOption(value = {"-C", "--color"}, defaultValue = CommandConstants.IMAGE_COLOR_NATURAL, help = CommandConstants.SHELL_OPTION_COLOR_MESSAGE) String color,
                                  @ShellOption(value = {"-P", "--path"}, help = CommandConstants.SHELL_OPTION_PATH_HELP_MESSAGE) String path) {
        logger.debug("Executing [{}] command with provided parameters, date [{}], color [{}], path [{}]", CommandConstants.SHELL_COMMAND_SAVE, date, color, path);

        if (!Files.exists(Paths.get(path))) {
            logger.error("Supplied path does not exist, [{}]", path);
            return "Supplied path does not exist, please use existing one.";
        }

        ImageList imageList = nasaEpicService.fetchImages(date, color);
        logger.debug("Downloaded images from NASA EPIC archive, count [{}]", imageList.getImages().size());

        imagePersistService.persistImages(imageList, path);

        return "Fetched " + imageList.getImages().size() + " images";
    }

}
