package com.ibm.security.appscan.altoromutual.servlet;
//Author: Group 5

//Utilizes Apache Commons, please make sure you refresh your gradle project to utilize the imports or this will NOT work.
import java.io.*;
import java.util.zip.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L; //These were in all the servlets, dont know why.
	
	private String uploadPath;
	@Override
    public void init() throws ServletException {
        // Get the real path of the 'uploads' directory inside the app
        String realPath = getServletContext().getRealPath("/uploads");
        if (realPath == null) {
            // Fallback to a default path if the real path can't be determined
            realPath = "uploads";
        }
        this.uploadPath = realPath;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // Create the directory if it doesn't exist
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(Integer.MAX_VALUE);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(Long.MAX_VALUE);
            upload.setSizeMax(Long.MAX_VALUE);

            File uploadDir = new File(uploadPath); //This sets the directory of where files will go.
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // This will create a folder following the above path if it doesnt exist within the app if configured carefully and correctly.
            }

            try {
                List<FileItem> formItems = upload.parseRequest(request); // Make a new list to parse upload/uploads (In case someone uploads a folder)
                for (FileItem item : formItems) { //For each FileItem within the list
                    if (!item.isFormField()) { //If the item is NOT just a fill in Form aka a File
                        String fileName = new File(item.getName()).getName(); //Get the files Name
                        File storeFile = new File(uploadPath, fileName); //Get the file

                        item.write(storeFile); // Save the file to directory

                        if (fileName.endsWith(".zip")) {
                            unzip(storeFile, uploadPath);
                        }

                        response.getWriter().print("File uploaded successfully to " + storeFile.getAbsolutePath()); 
                        //Tell the user the result and where the path is being upload (for us to know for now)
                        return;
                    }
                }
            } catch (Exception e) {
                response.getWriter().print("File upload failed.");
            }
        } else { //If user put nothing in upload and processed, tell them.
            response.getWriter().print("Invalid request.");
        }
    }

    public static void unzip(File zipFile, String uploadPath) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(uploadPath + File.separator + entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }
}