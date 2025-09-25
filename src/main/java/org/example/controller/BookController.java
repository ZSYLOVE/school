package org.example.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.example.entitys.Book;
import org.example.servise.BookService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/save")
    public SaResult save(@RequestPart("file") MultipartFile[] files, @RequestPart Book book) {
        return bookService.savaBook(files, book);
    }

    @GetMapping("/id")
    public SaResult selectook(@RequestParam Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/savaBookContent")
    public SaResult savaBookContent(@RequestPart("file") MultipartFile file, @RequestPart Long id) {
        return bookService.savaBookContent(file, id);
    }

    @PostMapping("/updateBook")
    public SaResult updateBook(@RequestPart(required = false) MultipartFile[] files, @RequestPart Book book) {
        return bookService.updateBook(files, book);
    }

    /**
     * 删除接口
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteBook")
    public SaResult deleteBook(@RequestParam Long id) {
        return bookService.deleteBook(id);
    }

    /**
     * 分页查询书籍
     *
     * @param page  页码，默认为1
     * @param size  每页数量，默认为10
     * @param title 书名关键词（可选）
     * @return SaResult 包含分页数据
     */
    @GetMapping("/page")
    public SaResult pageBook(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title) {

        Map<String, Object> searchMap = new HashMap<>();
        if (title != null && !title.trim().isEmpty()) {
            searchMap.put("title", title.trim());
        }
        return bookService.pageBook(page, size, searchMap);
    }
}