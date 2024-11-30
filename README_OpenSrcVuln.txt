Description
------------------------------------------------------------------------------------------------------------------------------------
WARNING: The implementation of the openSrcLibrary chart feature comes with introduces vulnerabilies into the web app.
Practice this vulnerability with caution in a controlled environment. This vulnerability demonstrates the risks associated with 
implementing unsecure open source libraries. There are lines of solution code to remedy these.

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Changes within AltoroJ-AltoroJ-3.2
------------------------------------------------------------------------------------------------------------------------------------
Changed code AltoroJ-AltoroJ-3.2/WebContent/bank/transaction.jsp //OpenSrcVul (chart) frontend implementation feature
Changed code AltoroJ-AltoroJ-3.2/WebContent/bank/ //created chartJSLibrary folder to house the insecure library used
  this folders, there is the corresponding library js file in it

Within the transactions.jsp code, there is a new chart that gives the end user a better experience viewing their most recent transactions. 
However, the library used to implement this chart could help and could hurt the web app security. We have demonstrated both sides. 
In WebContent/bank/chartJSLibrary/Chart.js, we see the vulnerable chart js library that is being used. According to the NVD, the vulnerability
this library introduces in a 9.8 critical vulnerably. 
In WebContent/bank/transactions.jsp, we see the most up-to-date chart js library that can also be used. This library doesn't have
known vulnerabilities and is secure. We can guarantee this library will remain up to date because we are reaching out to a CDN that 
will continue to point to the most up-to-date and most secure version of chart js

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Configuration
------------------------------------------------------------------------------------------------------------------------------------
Depending on the name of your folders in the WebContent/bank folder, you may have to alter the transaction.jsp file
The line holding: <script src="chartJSLibrary/Chart(1).js"></script> will need to have the src path following the correct, relative path to these library files.

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Practice
------------------------------------------------------------------------------------------------------------------------------------
When practicing this code, the code will change the transaction web page. It will now show a chart to provide a better end user experience for them using the web app.

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Test Cases with Solution Code uncommented
------------------------------------------------------------------------------------------------------------------------------------
The following test cases have been tested and verified:

Passing Cases: 
The vulnerbale version of the chart shows up in Microsoft Edge, Firefox, and Google Chrome
The secure version of the chart shows up in Microsoft Edge, Firefox, and Google Chrome

Unpassing Cases:
None
