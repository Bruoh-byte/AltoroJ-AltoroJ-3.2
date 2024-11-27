Description
------------------------------------------------------------------------------------------------------------------------------------
WARNING: The implementation of the fileUpload feature comes with unhindered upload vulnerabiltiies and path traversal vulnerabilities.
Practice these vulnerabilties with caution in a controlled environment. These vulnerabilities demonstrate risks in implementation of file upload functionality alongside
improper checks of path implementation of files. There are lines of solution code to remedy these.
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Changes within AltoroJ-AltoroJ-3.2
------------------------------------------------------------------------------------------------------------------------------------
Changed code AltoroJ-AltoroJ-3.2/build.gradle //Implementation of libraries
Added Class FileUploadServlet code AltoroJ-AltoroJ-3.2/src/com/ibm/security/appscan/altoromutual/servlet/FileUploadServlet.java //File Upload Code
Changed code AltoroJ-AltoroJ-3.2/WebContent/bank/transfer.jsp //File Upload frontend implementation feature
Changed code AltoroJ-AltoroJ-3.2/WebContent/WEB-INF/web.xml //Servlet mapping

build.gradle is changed to add the implementation of open source library ApacheCommonsFileUpload and ApacheCommonsIO

Within the code, there is a new servlet called FileUploadServlet.java that handles the uploading of files, including zip files, the code is well documented and 
has verbose comments describing the various aspects on how the code works to allow you to make some changes of your own with an understanding of the program.
This code utilizes open source library ApacheCommonsFileUpload to handle FileUpload functionality alongside zipfile decompression and writing.

transfer.jsp is changed to have half the page dedicated to the original transfer money functionality of AltoroJMutual and the other half to allow the uploading of
"Checks" when in truth, the feature allows the implemenentation of uploading all files, no matter the type and size utilizing open source library ApacheCommons FileUpload.

web.xml is changed to map the implementation of the FileUploadServlet.java servlet to then be read by frontend logic found within transfer.jsp
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Configuration
------------------------------------------------------------------------------------------------------------------------------------
Depending on the name of your root folder that holds the AltoroJ program, you may have to alter transfer.jsp
The line holding: <form action = "/AltoroJ-AltoroJ-3.2/fileupload" method="POST" enctype="multipart/form-data">
If your source folder is not named AltoroJ-AltoroJ-3.2, just change the code to read from the directory you have set the folder to be. Otherwise, when unzipped
it should compile
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Practice
------------------------------------------------------------------------------------------------------------------------------------
When practicing this code, the code will take you to a new page to give you the directory of where your uploads go so you can find them and see them in practice.
A folder will provide test files to upload that have test cases below written out.
However, path traversal must be practiced with caution, a zip file will be provided to demonstrate uploading to the util folder found within
AltoroJ-AltoroJ-3.2/WebContent/util/
A zip file called PathTraversaltoUtil.zip has a text file named '../util/I_arrive.txt'
This will put the text file within /util to show PathTraversal
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Test Cases with Solution Code uncommented
------------------------------------------------------------------------------------------------------------------------------------
There is a folder in AltoroJ-AltoroJ-3.2 called Demonstration Folder
PNGandJPGTests holds a png of 59KB of a Sample Check and JPG of 6 MB painting, former will pass, latter will fail
ZipTestFiles holds PathTraversaltoUtil.zip to demo Path Traversal,ZipBombExample.zip, Tester.zip
ZipBombExample is a compressed 16 mb compressed/2.25 gb uncompressed folder that excersizes resource exhaustion and storage, it is full of txt files with zeroes.
PathTraversaltoUtil.zip is a zip file to excersize Path Traversal to the Util folder.
Tester.zip is a testzip folder with a folder with two items, the png of Sample Check PNG of 59 kb, and a blank txt file called Tester.txt

The following test cases have been handled by the solution code found within FileUploadServlet.java

Passing Cases:
.zip files under 5MB
.zip files which contents are under 5 MB
uploading of .png, .jpg, .zip
.png .jpg under 5MB
.zip files which contents are .png and .jpg

Failing Cases:
.zip files which contents are not .png and .jpg
.zip files over 5MB
.zip files which contents are over 5MB
non allowed file types
.png .jpg over 5MB
path traversal demo
