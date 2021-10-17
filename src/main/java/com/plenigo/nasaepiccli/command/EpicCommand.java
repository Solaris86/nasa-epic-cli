package com.plenigo.nasaepiccli.command;

import com.plenigo.nasaepiccli.constants.CommandConstants;
import com.plenigo.nasaepiccli.model.ImageColor;
import com.plenigo.nasaepiccli.model.ImageRequestContext;
import com.plenigo.nasaepiccli.model.ImageResponseContext;
import com.plenigo.nasaepiccli.model.ImageType;
import com.plenigo.nasaepiccli.service.ImagePersistService;
import com.plenigo.nasaepiccli.service.NasaEpicService;
import com.plenigo.nasaepiccli.validation.ValidCapturedDate;
import com.plenigo.nasaepiccli.validation.ValidFilePath;
import com.plenigo.nasaepiccli.validation.ValidImageColor;
import com.plenigo.nasaepiccli.validation.ValidImageType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class EpicCommand {

    private static final Logger logger = LoggerFactory.getLogger(EpicCommand.class);

    private final NasaEpicService nasaEpicService;
    private final ImagePersistService imagePersistService;

    @ShellMethod(key = CommandConstants.SHELL_COMMAND_SAVE, value = "Fetches images from NASA EPIC API and saves them in a provided folder")
    public String saveEpicImages(@ShellOption(value = {"-D", "--date"}, defaultValue = "", help = CommandConstants.SHELL_OPTION_DATE_HELP_MESSAGE) @ValidCapturedDate String date,
                                 @ShellOption(value = {"-C", "--color"}, defaultValue = CommandConstants.IMAGE_COLOR_NATURAL, help = CommandConstants.SHELL_OPTION_COLOR_MESSAGE) @ValidImageColor String color,
                                 @ShellOption(value = {"-T", "--type"}, defaultValue = CommandConstants.IMAGE_TYPE_NAME_JPG, help = CommandConstants.SHELL_OPTION_TYPE_MESSAGE) @ValidImageType String type,
                                 @ShellOption(value = {"-P", "--path"}, help = CommandConstants.SHELL_OPTION_PATH_HELP_MESSAGE) @ValidFilePath String path) {
        logger.debug("Executing [{}] command with provided parameters, date [{}], color [{}], type [{}], path [{}]",
                CommandConstants.SHELL_COMMAND_SAVE, date, color, type, path);

        ImageRequestContext fetchContext = ImageRequestContext.builder()
                .date(date)
                .imageType(ImageType.forType(type))
                .imageColor(ImageColor.forColor(color))
                .build();
        ImageResponseContext imageResponseContext = nasaEpicService.fetchImages(fetchContext);
        logger.debug("Successfully downloaded images from NASA EPIC archive, count [{}]", imageResponseContext.getImages().size());

        imagePersistService.persistImages(imageResponseContext, path);

        return imageResponseContext.getImages().size() + " images have been successfully downloaded and saved";
    }

}
