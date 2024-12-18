package com.ibm.security.appscan.altoromutual.servlet;
//Author: Group 5
/*The following Servlet handles FileUploads for the intention of uploading checks however has multiple vulnerabilities
 * Frontend logic calling this is located within transfer.jsp and mapped accordingly within web.xml
 * The vulnerabilities are: No Checking for file types, and allowing the maximum possible upload size, as well as path traversal through the unzip method
 * Solution: Specify JPEGS and PNG uploads, as well as cap the size to something reasonable, such as 5 MB or (1024 * 1024 * 5) bytes, then in the unzip method >
 * Sanitize the file names for ../ and ensure their canonical path is the upload directory we desire or deny it, this is an efficient mitigation method for preventing path traversal
 */
//Utilizes Apache Commons, please make sure you refresh your gradle project to utilize the imports or this will NOT work.
import java.io.*;
import java.util.zip.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/*The following Servlet handles FileUploads for the intention of uploading checks however has multiple vulnerabilities
 * Frontend logic calling this is located within transfer.jsp and mapped accordingly within web.xml
 * The vulnerabilities are: No Checking for file types, and allowing the maximum possible upload size, as well as path traversal through the unzip method
 * Solution: Specify JPEGS and PNG uploads, as well as cap the size to something reasonable, such as 5 MB or (1024 * 1024 * 5) bytes, then in the unzip method >
 * Sanitize the file names for ../ and ensure their canonical path is the upload directory we desire or deny it, this is an efficient mitigation method for preventing path traversal
 */
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L; //These were in all the servlets, dont know why.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        if (ServletFileUpload.isMultipartContent(request)) { //Is the request hitting this backend logic an upload? In the case someone hits upload without a file.
            DiskFileItemFactory factory = new DiskFileItemFactory(); //Make a DiskFileItemFactory to handle File Requests
            
            factory.setSizeThreshold(Integer.MAX_VALUE); // Vulnerable code, comment and replace with below REPLACE
//            factory.setSizeThreshold(1024 * 1024 * 1);	// Solution code to fix File Upload max Size SOLUTION             
            ServletFileUpload upload = new ServletFileUpload(factory); //ServletFileUpload Object handles the transportation of file requests, utilizing factory.
            upload.setFileSizeMax(Long.MAX_VALUE); // Vulnerable max size, comment and replace with below REPLACE
            upload.setSizeMax(Long.MAX_VALUE); // Vulnerable max size, comment and replace with below REPLACE
//              upload.setFileSizeMax(1024 * 1024 * 5); //  Solution code to fix upload size for entire request SOLUTION 
//  			  upload.setFileSizeMax(1024 * 1024 * 5); // Solution code to fix upload size for entire request SOLUTION
            String finalPath = getServletContext().getRealPath("/checks");
            
            String uploadPath = finalPath; // Set Upload Path here
            File uploadDir = new File(uploadPath); //This sets the directory of where files will go.
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // This will create a folder following the above path if it doesnt exist within the app if configured carefully and correctly.
            }
            try {
                List<FileItem> formItems = upload.parseRequest(request); // Make a new list to parse upload/uploads (In case someone uploads a folder) / Multipart requests break down complex files
                for (FileItem item : formItems) { //For each FileItem within the list
                    if (!item.isFormField()) { //If the item is a file, which it should be, do logic
                        String fileName = new File(item.getName()).getName(); //Get the files Name, we need this for file checking
                        File storeFile = new File(uploadPath, fileName); //Get the file and its path
                        
 //                      if (!(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".zip"))) { // Check to make sure it is within bounds SOLUTION
 //                    	throw new IOException("Invalid File Type"); // Throw the error SOLUTION
 //                       } // SOLUTION
                        
                        
                  
                        item.write(storeFile); // Save the file to directory / Yes even the ZIP for case check

                        
                        
                        
                        if (fileName.endsWith(".zip")) { // Send it away for Path Traversal Vulnerability!!! :D
                        	unzip(storeFile, uploadPath);
                        }
                        
                        
                        response.getWriter().print("File uploaded successfully to " + storeFile.getAbsolutePath()); //Tell the user the result, takes them to a new page, couldnt figure out the frontend stuff
                        return; // Once uploaded, exit via return
                    }
                }
            } 
            catch (Exception e) {
                response.getWriter().print("File upload failed.");
            }
        } 
        else { //If user put nothing in upload and processed, tell them.
            response.getWriter().print("Invalid request.");
        }
    }
    private static void unzip(File zipFile, String uploadPath) throws IOException {
//     	  File trueUploadPath = new File(uploadPath); //This is a dummy file made to make path traversal comparison SOLUTION
//		  long maxTotalSize = 1024 * 1024 * 5; //Set max size SOLUTION
//        long runningTotalSize = 0; // set runningtotal SOLUTION
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) { //Take the zipfile into ZipInputStream Javas built in Zip Handling. compressed and uncompressed entries
            ZipEntry entry; // Javas built in Zip Entry Object
            byte[] buffer = new byte[1024]; // This buffer array handles all the bytes that come through when reading files, cause again, files are transfered via 1s and 0s, make this bigger if you have good performance
            while ((entry = zis.getNextEntry()) != null) { // Retrieve the next available file or directory in the zip file
                File newFile = new File(uploadPath, entry.getName()); //Upload path + / + file name using the 2nd File Constructor utilizing just a string
//                String entryName = entry.getName(); //Get the file name SOLUTION
//                if (!(entryName.endsWith(".png") || entryName.endsWith(".jpg"))) { // Check that an entry is a JPG or PNG, if it isnt, skip it SOLUTION
//              	continue; // Skip it, go to the next entry SOLUTION
//                 } // SOLUTION
//                String whereIsFileGoing = newFile.getCanonicalPath(); // Get a string idea of the given file path SOLUTION
//                String whereShouldFileBe = trueUploadPath.getCanonicalPath(); // Get where the path of where the file should go SOLUTION
//                if (!whereIsFileGoing.startsWith(whereShouldFileBe + File.separator)) { //This will check that the path it will end up is where it belongs, and not outside directory SOLUTION
//                  throw new IOException("File is going outside of its destination"); //Throw the error SOLUTION
//                } //SOLUTION
                if (entry.isDirectory()) { // Is it a folder?
                    newFile.mkdirs(); // Make it
                } else {
                    new File(newFile.getParent()).mkdirs(); //Make sure its parent exists, or else you have an orphan file who may not go where they belong
                    
//                    long entrySize = entry.getSize(); // Get its uncompressed size SOLUTION
//                    runningTotalSize += entrySize; // Add it to the running total SOLUTION
                    
 //                   if (runningTotalSize > maxTotalSize) { // Check it SOLUTION
 //                   	throw new IOException("Too large files within"); //Too large? Error SOLUTION
 //                   } // SOLUTION
                    
                    try (FileOutputStream fos = new FileOutputStream(newFile)) { // Make file in the desired directory, PATH TRAVERSAL VULNERABILITY
                        int length;
                        while ((length = zis.read(buffer)) > 0) { //Read the compressed files size and while its not zero, start writing it together
                            fos.write(buffer, 0, length); //Write the file in the respective binary, when its all done, the 1s and 0s in the right arrangement make your file :)
                        }
                    }
                }
            }
        }
    }
}
