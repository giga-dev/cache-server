package org.gigaspaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class RestController {

    @Autowired
    private FileService fileService;

    @Value("${remote.base.url}")
    private String remoteBaseUrl;
//
////    @GetMapping(value = "/**", produces = "application/zip")
//    public
//    void greeting2(final HttpServletRequest request, final HttpServletResponse response) throws URISyntaxException, InterruptedException, IOException {
//        final String url = request.getRequestURI();
//
//        File file = fileService.get(remoteBaseUrl + url);
//
//        response.setStatus(HttpServletResponse.SC_OK);
////        response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");
//
//        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
//        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
//        FileInputStream fileInputStream = new FileInputStream(file);
//
//        IOUtils.copy(fileInputStream, zipOutputStream);
//
//        fileInputStream.close();
//        zipOutputStream.closeEntry();
//
//        zipOutputStream.close();
//    }
//
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if(statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error-404";
//            }
//            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return "error-500";
//            }
//        }
//        return "error";
//    }

    @GetMapping(value = "/**")
    public ResponseEntity<InputStreamResource> downloadFile(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        try {
            File file = fileService.get(remoteBaseUrl + url);

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok().contentLength(file.length())
                    .body(resource);
        } catch (IOException | URISyntaxException e) {
            return ResponseEntity.notFound().build();
//            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "File " + url+" does not exist");
        }
    }




}

