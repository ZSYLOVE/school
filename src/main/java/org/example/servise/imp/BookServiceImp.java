package org.example.servise.imp;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.entitys.Book;
import org.example.entitys.File;
import org.example.entitys.FileProperties;
import org.example.entitys.User;
import org.example.mapper.BookMapper;
import org.example.mapper.FileMapper;
import org.example.mapper.UserMapper;
import org.example.servise.BookService;
import org.example.util.FilesUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@Slf4j
public class BookServiceImp extends ServiceImpl<BookMapper, Book> implements BookService {

    private final FilesUtils filesUtils;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final FileMapper fileMapper;
    private final FileProperties fileProperties;

    public BookServiceImp(FilesUtils filesUtils, BookMapper bookMapper, UserMapper userMapper, FileMapper fileMapper, FileProperties fileProperties) {
        this.filesUtils = filesUtils;
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
        this.fileProperties = fileProperties;
    }

    /**
     * 保存书籍
     * @param files
     * @param book
     * @return
     */
    @Override
    public SaResult savaBook(MultipartFile[] files, Book book) {
        ArrayList<String> filePath=new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> upload = filesUtils.upload(file);
            if (upload.get("filePath") == null) {return SaResult.error("请传递.jpg,.jpeg,.png格式的图片");}
            filePath.add(upload.get("filePath").toString());
        }
        book.setCoverImage(filePath.get(0));
        book.setBackImage(filePath.get(1));
        //保存当前用户
        String loginPhone = StpUtil.getLoginId().toString();
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,loginPhone);
        User user = userMapper.selectOne(wrapper);
        if (user==null) return SaResult.error("未查询出当前用户");
        book.setSubmitUser(user.getId());
        return bookMapper.insert(book)>0?SaResult.ok("保存成功"):SaResult.error("保存失败");
    }
    /**
     * 查看书籍
     * @param id
     * @return
     */
    @Override
    public SaResult getBookById(Long id) {
        Book book = bookMapper.selectById(id);
        if (book==null) return SaResult.error("未查询出书籍信息");
        book.setCoverImage(book.getCoverImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
        book.setBackImage(book.getBackImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getBookId, id);
        File file = fileMapper.selectOne(queryWrapper);
        if (file!=null) book.setFileName(file.getPathFileName().replace(fileProperties.getPdfAddress(),fileProperties.getPdfHttpAddress()));
        return SaResult.data(book);
    }
    /**
     * 保存书籍内容
     * @param file
     * @param id
     * @return
     */
    @Override
    public SaResult savaBookContent(MultipartFile file, Long id) {
        Book book = bookMapper.selectById(id);
        if (book==null) return SaResult.error("未有已保存的书籍信息");
        String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!".pdf".equals(substring)) return SaResult.error("请传递pdf格式的书籍内容文件");
        Map<String, Object> upload = filesUtils.upload(file);
        Object filePath = upload.get("filePath");
        File build = File.builder().bookId(id).type(0).pathFileName(filePath.toString()).build();
        return fileMapper.insert(build)>0?SaResult.ok("保存成功"):SaResult.error("保存失败");
    }
    /**
     * 修改书籍信息
     * @param files
     * @param book
     * @return
     */
    @Override
    public SaResult updateBook(MultipartFile[] files, Book book) {

        Book book1 = bookMapper.selectById(book.getId());
        if(book1==null) return  SaResult.error("未查询出要修改的书籍信息");
        if (files!=null&&files[0]!=null){
            Map<String, Object> upload = filesUtils.upload(files[0]);
            if (upload.get("filePath") == null) return SaResult.error("未按照规定格式传递文件");
            book.setCoverImage(upload.get("filePath").toString());
        }
        if (files!=null&&files[1]!=null){
            Map<String, Object> upload = filesUtils.upload(files[0]);
            if (upload.get("filePath") == null) return SaResult.error("未按照规定格式传递文件");
            book.setBackImage(upload.get("filePath").toString());
        }
        return bookMapper.updateById(book)>0?SaResult.ok("修改成功"):SaResult.error("修改失败");
    }
    /**
     * 删除书籍
     * @param id
     * @return
     */
    @Override
    public SaResult deleteBook(Long id) {
        Book book = bookMapper.selectById(id);
        if(book==null) return SaResult.error("未找到需要删除的书籍信息");
        String coverImage = book.getCoverImage();
        String backImage = book.getBackImage();
        filesUtils.deleteFile(coverImage);
        filesUtils.deleteFile(backImage);
        //查询是否有上传书籍内容信息
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getBookId,book.getId());
        File file = fileMapper.selectOne(queryWrapper);
        if(file!=null){
            filesUtils.deleteFile(file.getPathFileName());
            fileMapper.delete(queryWrapper);
        }
        bookMapper.deleteById(id);
        return SaResult.ok("删除成功");
    }

    /**
     * 分页查询书籍
     * @param page 当前页码
     * @param size 每页大小
     * @param searchMap 查询条件
     * @return
     */
    @Override
    public SaResult pageBook(Integer page, Integer size, Map<String, Object> searchMap) {
        Page<Book> bookPage = new Page<>(page, size);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        // 可以根据需要添加查询条件
        if (searchMap != null && !searchMap.isEmpty()) {
            // 示例：根据标题搜索
            Object title = searchMap.get("title");
            if (title != null && !title.toString().isEmpty()) {
                queryWrapper.like(Book::getTitle, title.toString());
            }
        }
        Page<Book> result = bookMapper.selectPage(bookPage, queryWrapper);
        return SaResult.data(result);
    }

    @Override
    public List<String> getImagesNames() {
        List<String> list = new ArrayList<>();
        List<Book> books = bookMapper.selectList(null);
        for (Book book : books) {
            list.add(book.getBackImage().substring(book.getBackImage().lastIndexOf("/") + 1));
            list.add(book.getCoverImage().substring(book.getCoverImage().lastIndexOf("/") + 1));
        }
        return list;
    }
}




