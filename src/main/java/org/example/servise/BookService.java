package org.example.servise;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entitys.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BookService extends IService<Book> {
    SaResult savaBook(MultipartFile[] files, Book book);

    SaResult getBookById(Long id);

    SaResult savaBookContent(MultipartFile file, Long id);

    SaResult updateBook(MultipartFile[] files, Book book);

    SaResult deleteBook(Long id);
    
    SaResult pageBook(Integer page, Integer size, Map<String, Object> searchMap);
}