package org.example.ImgQuartzJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entitys.FileProperties;
import org.example.servise.BookService;
import org.example.util.FilesUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ImgQuartzJob implements Job {
    private final BookService bookService;
    private final FilesUtils filesUtils;
    private final FileProperties fileProperties;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<String> imagesNames = bookService.getImagesNames();
            if (imagesNames.isEmpty()) return;
            List<String> imgs = filesUtils.listFileNames(fileProperties.getImgAddress());
            if (imgs.isEmpty()) return;
            // 找出 imagesNames 中有，但 strings 中没有的
            List<String> difference = new ArrayList<>(imgs);
            difference.removeAll(imagesNames);
            for (String name : difference) {
                filesUtils.deleteFile(fileProperties.getImgAddress()+name);
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}