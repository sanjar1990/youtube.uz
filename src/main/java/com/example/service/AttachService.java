package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exception.ItemNotFoundException;
import com.example.repository.AttachRepository;
import com.xuggle.xuggler.IContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class AttachService {
     @Autowired
     private AttachRepository attachRepository;
    @Value("${attach.folder.name}")
    private String folderName;
    @Value("${attach.url}")
    private String attachUrl;
    public AttachDTO upload(MultipartFile file) {
        if(file.isEmpty()) throw new ItemNotFoundException("file not found");
        String pathFolder=getYMD();
        File folder=new File(folderName+"/"+pathFolder);
        if(!folder.exists()){
            folder.mkdirs();
        }
    String key= UUID.randomUUID().toString();
    String extension=getExtension(file.getOriginalFilename());
    try {
    byte[] bytes=file.getBytes();
    Path path= Paths.get(folderName+"/"+pathFolder+"/"+key+"."+extension);
        Files.write(path,bytes);
        AttachEntity attach=new AttachEntity();
        AttachDTO dto=new AttachDTO();
        if(extension.equals("mp4") ||extension.equals("avi")){
            IContainer container=IContainer.make();
            container.open(path.toString(),IContainer.Type.READ,null);
            long duration=container.getDuration();
            attach.setDuration(duration/1000);
            dto.setDuration(duration);
            container.close();
        }
        attach.setId(key);
        attach.setSize(file.getSize());
        attach.setExtension(extension);
        attach.setOriginName(file.getOriginalFilename());
        attach.setPath(pathFolder);
        attachRepository.save(attach);

        dto.setId(key);
        dto.setType(extension);
        dto.setPath(pathFolder);
        dto.setSize(file.getSize());
        dto.setCreatedDate(attach.getCreatedDate());
        dto.setUrl(getUrl(key));
        dto.setOriginName(file.getOriginalFilename());
        return dto;
    }catch (IOException e){
        throw new RuntimeException(e);
    }
    }
//    2. Get Attach By Id (Open)
    public byte[] openById(String id) {
        AttachEntity entity=get(id);
        try {
            BufferedImage originalImage= ImageIO.read(new File(url(entity.getPath(),entity.getId(),entity.getExtension())));
            ByteArrayOutputStream boas=new ByteArrayOutputStream();
            ImageIO.write(originalImage,entity.getExtension(),boas);
            boas.flush();
            byte [] imageInByte=boas.toByteArray();
            boas.close();
            return imageInByte;
        }catch (Exception e){
        return new byte[0];
        }
    }
    //open by id general
    public byte[] loadByIdGeneral(String id) {
        AttachEntity entity=get(id);
        try {
            File file=new File(url(entity.getPath(),entity.getId(),entity.getExtension()));
            byte[]bytes=new byte[(int) file.length()];
            FileInputStream fileInputStream=new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
            return bytes;
        }catch (Exception e){
            e.printStackTrace();
            return new byte[0];
        }
    }
    //3. Download Attach (Download)

    public ResponseEntity<Resource> download(String id) {
        AttachEntity entity = get(id);
        try {
            Path file = Paths.get(url(entity.getPath(), entity.getId(), entity.getExtension()));
            org.springframework.core.io.Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    // 4. Attach pagination (ADMIN)
    public PageImpl<AttachDTO> pagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<AttachEntity>pageObj=attachRepository.findAll(pageable);
        List<AttachDTO> dtoList=pageObj.getContent().stream().map(s->{
            AttachDTO dto=new AttachDTO();
            dto.setId(s.getId());
            dto.setOriginName(s.getOriginName());
            dto.setSize(s.getSize());
            dto.setUrl(getUrl(s.getId()));
            return dto;
        }).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    // 5. Delete Attach (delete from db and system) (ADMIN)
    public ApiResponseDTO delete(String id) {
        boolean t=false;
        AttachEntity entity=get(id);
        File file=new File(url(entity.getPath(),entity.getId(),entity.getExtension()));
        if(file.exists()){
            t=file.delete();
        }
        attachRepository.deleteById(id);
        return t? new ApiResponseDTO(false,"deleted"):new ApiResponseDTO(true,"not deleted");
    }
    private String url(String path, String id, String extension){
        return folderName+"/"+path+"/"+id+"."+extension;
    }
    public String getUrl(String id){
        return attachUrl+"/public/"+id+"/img";
    }

    private String getYMD(){
    int year= Calendar.getInstance().get(Calendar.YEAR);
    int month=Calendar.getInstance().get(Calendar.MONTH);
    int day=Calendar.getInstance().get(Calendar.DATE);
        return year+"/"+month+"/"+day;
    }
    private String getExtension(String fileName){
    int lastIndex=fileName.lastIndexOf(".");
    return fileName.substring(lastIndex+1);

    }
    public AttachEntity get(String id){
        return attachRepository.findById(id).orElseThrow(()->new ItemNotFoundException("Attach not found"));
    }



}
