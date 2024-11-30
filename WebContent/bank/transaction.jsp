<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

   
<jsp:include page="/header.jspf"/>

<div id="wrapper" style="width: 99%;">
	<jsp:include page="membertoc.jspf"/>
    <td valign="top" colspan="3" class="bb">
    
		<%@page import="java.util.Date"%>
		<%@page import="com.ibm.security.appscan.altoromutual.model.Transaction"%>
		
		<div class="fl" style="width: 99%;">
		
	 	<%
		com.ibm.security.appscan.altoromutual.model.User user = (com.ibm.security.appscan.altoromutual.model.User)request.getSession().getAttribute("user");
		String startString = request.getParameter("startTime");
		String endString = request.getParameter("endTime");

		String error = "";
			 			
		Transaction[] transactions = new Transaction[0];
			 			
		transactions = user.getUserTransactions(startString, endString, user.getAccounts());

		%>
		
		<h1>Recent Transactions</h1>
		
		<script type="text/javascript">
			function confirminput(myform) {
			
				if (myform.startDate.value != ""){
					var valid = false;
					var splitStrings = myform.startDate.value.split("-");
					if (splitStrings.length == 3) {
						var year = parseInt(splitStrings[0]);
						var month = parseInt((splitStrings[1].charAt(0)==0 && splitStrings[1].length == 2)?splitStrings[1].charAt(1):splitStrings[1]);
						var day = parseInt((splitStrings[2].charAt(0)==0 && splitStrings[2].length == 2)?splitStrings[2].charAt(1):splitStrings[2]);

						var validNums = !(isNaN(year) || isNaN(month) || isNaN(day));
						
						if (validNums)
							valid = validateDate(month, day, year);
					}
			
					if (!valid){
						alert ("'After' date of " + myform.startDate.value + " is not valid.");
						return false;
					}
				}
			
				if (myform.endDate.value != ""){
					var valid2 = false;
					var splitStrings2 = myform.endDate.value.split("-");
					if (splitStrings2.length == 3) {
						var year2 = parseInt(splitStrings2[0]);
						var month2 = parseInt((splitStrings2[1].charAt(0)==0 && splitStrings2[1].length == 2)?splitStrings2[1].charAt(1):splitStrings2[1]);
						var day2 = parseInt((splitStrings2[2].charAt(0)==0 && splitStrings2[2].length == 2)?splitStrings2[2].charAt(1):splitStrings2[2]);
			
						var validNums2 = !(isNaN(year2) || isNaN(month2) || isNaN(day2));
						
						if (validNums2)
							valid2 = validateDate(month2, day2, year2);
					}
					
					if (!valid2){
						alert ("'Before' date of " + myform.endDate.value + " is not valid.");
						return false;
					}
				}
				return true;
			}
			
			function validateDate(month, day, year){
				try {
					var thisDate = new Date();
					var wrongMonth = month<1 || month>12;
					var wrongDay = (day<1) || (day>31) || (day>30 && ((month==4)||(month==6)||(month==9)||(month==11))) || (day>29 && month==2 && (year%4==0) && (year%100!=0 || year%400==0)) || (day>28 && month==2 && ((year%4!=0) || (year%100==0 && year%400!=0))); 
					var wrongYear = year < 1990 || year > parseInt(thisDate.getFullYear());
			
					var thisYear = parseInt(thisDate.getFullYear());
					var thisMonth = parseInt(thisDate.getMonth())+1;
					var thisDay = parseInt(thisDate.getDate());
					var wrongDate = year==thisYear && ((thisMonth<month) || (thisMonth==month && thisDay<(day-1)));
			
					if (wrongMonth ||wrongDay || wrongYear || wrongDate)
						return false;
					
					} catch (error){
						return false;
					}
					
					return true;
			}
		</script>
		
		<font style="bold" color="red"><%=error%></font>
		<form id="Form1" name="Form1" method="post" action="showTransactions" onsubmit="return (confirminput(Form1));">
		<table border="0" style="padding-bottom:10px;">
		    <tr>
		        <td valign=top>After</td>
		        <td><input id="startDate" name="startDate" type="text" value="<%=(request.getParameter("startDate")==null)?"":request.getParameter("startDate")%>"/><br /><span class="credit">yyyy-mm-dd</span></td>
		        <td valign=top>Before</td>
		        <td><input name="endDate" id="endDate" type="text" value="<%=(request.getParameter("endDate")==null)?"":request.getParameter("endDate") %>"/><br /><span class="credit">yyyy-mm-dd</span></td>
		        <td valign=top><input type=submit value=Submit /></td>
		    </tr>
		</table>
		
		<table cellspacing="0" cellpadding="3" rules="all" border="1" id="_ctl0__ctl0_Content_Main_MyTransactions" style="width:100%;border-collapse:collapse;">
			<tr style="color:White;background-color:#BFD7DA;font-weight:bold;">
				<td>Transaction ID</td><td>Transaction Time</td><td>Account ID</td><td>Action</td><td>Amount</td>
			</tr>
			<% for (int i=0; i<transactions.length; i++){
				//limit to 100 entries
				if (i==100)
					break;
				
				double dblAmt = transactions[i].getAmount();
				String format = (dblAmt<1)?"$0.00":"$.00";
				String amount = new DecimalFormat(format).format(dblAmt);
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(transactions[i].getDate());
			%>
		
				<tr><td><%=transactions[i].getTransactionId()%></td><td><%=date%></td><td><%=transactions[i].getAccountId()%></td><td><%=transactions[i].getTransactionType()%></td><td align="right"><%=amount%></td></tr>
			<% } %>
		<tr>
		<!-- TODO PAGES: <td colspan="4"><span>1</span>&nbsp;<a href="javascript:__doPostBack('_ctl0$_ctl0$Content$Main$MyTransactions$_ctl54$_ctl1','')">2</a></td> -->
			</tr>
		</table>
		
			<!-- CapStone Project Changes Below -->
			
		    <canvas id="myChart" width="450" height="100"></canvas> <!-- creates canvas element called myChart -->
		    
 			<!-- Reference to Local Chart.js library below
		    *Pay Attention:
		    If you are testing for the vulnerability, you must use the insecure library called Chart.js
		    If you are testing for secureness, you must use the secure library called chart.umd.js
		    Which ever one you are testing for, comment the other script line out here AND comment out that file in the directory-->

			<!-- This uses the vulnerable library -->
		    <!-- <script src="chartJsLibrary/Chart.js"></script> -->	
		    
		    <!-- This uses modern, secure library  -->
		    <script type="module" src="chartJSLibrary_Secure/chart.umd.js"></script>
		    
		    <!-- Which ever one you are testing for, comment the other script line out above AND comment out that file in the directory -->
		    
		    <!-- Chart Details script: provides specific data about the chart -->
			<script>
			    document.addEventListener('DOMContentLoaded', function() { // Ensure the DOM is fully loaded before running the script
			    	 var ctx = document.getElementById('myChart').getContext('2d'); //gets the chart context
			         var myChart = new Chart(ctx, { //puts the chart data into the variable
			            type: 'line', //type of chart
			            data: { //data going into chart
			                labels: ['3/19/17', '3/19/17', '3/19/18', '3/19/18', '3/19/18', '3/19/18', '3/7/19', '3/7/19', '3/8/19', '3/11/19'], //x-axis data
			                datasets: [{
			                    label: 'Overview of all Withdraw/Deposit Activity', //chart title
			                    data: [-100.72, 100.72, -1100, 1100, -600.88, 600.88, -400, 400, -100, 100, -400, 400], //y-axis data
			                    backgroundColor: 'rgba(153, 172, 174, .3)', //chart background color
			                    borderColor: 'rgba(191, 215, 218, 1)', //chart border color
			                    borderWidth: 3, //chart border width
			                    fill: true //chart has fill
			                }]
			            },
			            options: {
			                scales: {
			                    y: { 
			                        ticks: {
			                            beginAtZero: true
			                        }
			                    },
			                    x: { 
			                        grid: {
			                            color: 'green'
			                        }
			                    }
			                }
			            }
			        });
			    }); //End of chart script
			</script>
			<!-- CapStone Project Changes Finished -->
		
		</form>
		
		</div>    
    
    </td>	
</div>

<div> 


</div>


<jsp:include page="/footer.jspf"/>  
