$(document).bind("mobileinit", function() {
　　$.mobile.defaultPageTransition = "fade";
　　$.mobile.defaultDialogTransition = "fade";
　　});

function indexToNewproduct(){
	window.location.href="/newproduct.html";
}
function indexToQuestionnaire(){
	window.location.href="/questionnaire.html";
}
function indexToListProvinces(){
	window.location.href="/listProvinces.html";
}
function getcity(province){
	$.mobile.changePage("/cityMaps.html?province="+province, "slideup"); 
}
function returnMain() {
	// $.mobile.changePage("/");
	window.location.href="/";
}
function enterName(){
	window.location.href="/enterName.html";
}

function summary(){
	window.location.href="/summary.html"
}


function save() {
	var data = [];
	$.each($(".question1:checked"), function(i, n) {
		data[i] = n.value;
	});
	
	$("#result1").val(data);
	$.each($(".question2:checked"), function(i, n) {
		data[i] = n.value;
	});

	$("#result2").val(data);
	$.mobile.changePage("/news.NewsAction/saveResult", {
		type : "post",
		data : $("form#saveResponse").serialize(),
		transition : "slide"
	});
	alert("提交成功。");
}
