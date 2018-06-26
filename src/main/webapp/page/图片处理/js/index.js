// JavaScript Document
/*js代码比较繁琐大部分重复的可以简化为一个方法*/

 var grayscale=0;
	var sepia=0;
	var saturate=1;
	var huerotate=0;
	var invert=0;
	var opacity=1;
	var brightness=1;
	var contrast=1;
	var blurvalue=0;
	var dropshadowx=0;
	var dropshadowy=0;
	var dropshadowl=0;
	var dropshadowcolor="#fff";
	function stylea(){
		document.getElementsByName('button').item(1).className="losefocus";
	   document.getElementsByName('button').item(2).className="losefocus";
	   document.getElementsByName('button').item(3).className="losefocus";
	   document.getElementsByName('button').item(4).className="losefocus";
	   document.getElementsByName('button').item(5).className="losefocus";
	   document.getElementsByName('button').item(6).className="losefocus";
	   document.getElementsByName('button').item(7).className="losefocus";
	   document.getElementsByName('button').item(8).className="losefocus";
	   document.getElementsByName('button').item(9).className="losefocus";
	   document.getElementsByName('button').item(0).className="losefocus";
	}
   function css3_filter(id){
	   stylea();
	   var button=document.getElementById(id);
	   button.className="onclick";
	   sum();
	   var img=document.getElementsByTagName('img').item(0);
	   var value=document.getElementById('value');
	   switch(id){
		   case 'grayscale':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('output');
		   p.value=grayscale;
		   p.id="output";
		   input.setAttribute('value',""+grayscale);
		   input.setAttribute('type','range');
		   input.setAttribute('max','1');
		   input.setAttribute('min','0');
		   input.setAttribute('step','0.01');
		   input.setAttribute('onchange','grayscaleclick(this.value)');
		   input.setAttribute('way','grayscale');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'sepia':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('output');
		   p.value=sepia;
		   p.id="output"
		   input.setAttribute('type','range');
		   input.setAttribute('max','1');
		   input.setAttribute('min','0');
		   input.setAttribute('step','0.01');
		   input.setAttribute('onchange','sepiaclick(this.value)');
		   input.setAttribute('way','sepia');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'saturate':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('input');
		   p.type="number";
		   p.value=saturate;
		   p.id="output";
		   p.setAttribute('way','saturatetext');
		   p.setAttribute('oninput','saturateclick(this.value)');
		   input.setAttribute('type','range');
		   input.setAttribute('max','50');
		   input.setAttribute('min','0');
		   input.setAttribute('step','1');
		   input.setAttribute('onchange','saturateclick(this.value)');
		   input.setAttribute('way','saturate');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'hue-rotate':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('input');
		  p.type="number";
		   p.value=saturate;
		   p.id="output";
		   p.setAttribute('way','huerotatetext');
		   p.setAttribute('oninput','huerotateclick(this.value)');
		   input.setAttribute('type','range');
		   input.setAttribute('max','50');
		   input.setAttribute('min','0');
		   input.setAttribute('step','1');
		   input.setAttribute('onchange','huerotateclick(this.value)');
		   input.setAttribute('way','huerotate');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'invert':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('output');
		   p.value=invert;
		   p.id="output";
		   input.setAttribute('type','range');
		   input.setAttribute('max','1');
		   input.setAttribute('min','0');
		   input.setAttribute('step','0.01');
		   input.setAttribute('onchange','invertclick(this.value)');
		   input.setAttribute('way','invert');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'opacity':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('output');
		   p.value=invert;
		   p.id="output";
		   input.setAttribute('type','range');
		   input.setAttribute('max','1');
		   input.setAttribute('min','0');
		   input.setAttribute('step','0.01');
		   input.setAttribute('onchange','opacityclick(this.value)');
		   input.setAttribute('way','opacity');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'brightness':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('input');
		   p.type="number";
		   p.value=brightness;
		   p.id="output";
		   p.setAttribute('way','brightnesstext');
		   p.setAttribute('oninput','brightnessclick(this.value)');
		   input.setAttribute('type','range');
		   input.setAttribute('max','100');
		   input.setAttribute('min','0');
		   input.setAttribute('step','0.01');
		   input.setAttribute('onchange','brightnessclick(this.value)');
		   input.setAttribute('way','brightness');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'contrast':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('input');
		   p.type="number";
		   p.value=brightness;
		   p.id="output";
		   p.setAttribute('way','contrasttext');
		   p.setAttribute('oninput','contrastclick(this.value)');
		   input.setAttribute('type','range');
		   input.setAttribute('max','100');
		   input.setAttribute('min','0');
		   input.setAttribute('step','0.01');
		   input.setAttribute('onchange','contrastclick(this.value)');
		   input.setAttribute('way','contrast');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'blur':
		   value.innerHTML="";
		   var input=document.createElement('input');
		   var p=document.createElement('output');
		   p.value=blurvalue;
		   p.id="output"
		   input.setAttribute('type','range');
		   input.setAttribute('max','100');
		   input.setAttribute('min','0');
		   input.setAttribute('step','1');
		   input.setAttribute('onchange','blurclick(this.value)');
		   input.setAttribute('way','blur');
		   value.appendChild(input);
		   value.appendChild(p);
		   break;
		   case 'drop-shadow':
		   value.innerHTML="";
		   var px=document.createElement('output');
		   var py=document.createElement('output');
		   var pl=document.createElement('output');
		   var pc=document.createElement('output');
		   px.value="x偏移：";
		   py.value="y偏移：";
		   pl.value="长度：";
		   pc.value="颜色：";
		   var inputx=document.createElement('input');
		   var inputy=document.createElement('input');
		   var inputl=document.createElement('input');
		   var inputc=document.createElement('input');
		   inputx.setAttribute('type','number');
		   inputy.setAttribute('type','number');
		   inputl.setAttribute('type','number');
		   inputc.setAttribute('type','color');
		   inputx.setAttribute('oninput','dropshadowclickx(this.value)');
		   inputy.setAttribute('oninput','dropshadowclicky(this.value)');
		   inputl.setAttribute('oninput','dropshadowclickl(this.value)');
		   inputc.setAttribute('onchange','dropshadowclickc(this.value)');
		   value.appendChild(px);
		   value.appendChild(inputx);
		   value.appendChild(py);
		   value.appendChild(inputy);
		   value.appendChild(pl);
		   value.appendChild(inputl);
		   value.appendChild(pc);
		   value.appendChild(inputc);
		   
		   break;
		   case 'img':
		    grayscale=0;
	        sepia=0;
	        saturate=1;
	        huerotate=0;
	        invert=0;
	        opacity=1;
	        brightness=1;
	        contrast=1;
	        blurvalue=0;
	        dropshadowx=0;
	        dropshadowy=0;
	        dropshadowl=0;
			dropshadowcolor="#fff";
			value.innerHTML=" 属性值（点击图片取消所有效果）";
			stylea();
			sum();

		   break;
	   }
   }
   function sepiaclick(value){
	 sepia=value;
	 var img=document.getElementsByTagName('img').item(0);
	 sum();
	 document.getElementById('output').value=sepia;
   }
   function grayscaleclick(value){
	 grayscale=value;
	 var img=document.getElementsByTagName('img').item(0);
	sum();
	 document.getElementById('output').value=grayscale;
   }
   function saturateclick(value){
	 saturate=value;
	 var img=document.getElementsByTagName('img').item(0);
	sum();
	 document.getElementById('output').value=saturate;
   }
   function huerotateclick(value){
	 huerotate=value;
	 var img=document.getElementsByTagName('img').item(0);
	 sum();
	 document.getElementById('output').value=huerotate;
   }
   function invertclick(value){
	 invert=value;
	 var img=document.getElementsByTagName('img').item(0);
	 sum();
	 document.getElementById('output').value=invert;
   }
    function opacityclick(value){
	 opacity=value;
	 var img=document.getElementsByTagName('img').item(0);
	sum();
	 document.getElementById('output').value=opacity;
   }
   function brightnessclick(value){
	 brightness=value;
	 var img=document.getElementsByTagName('img').item(0);
	 sum();
	 document.getElementById('output').value=brightness;
   }
   function contrastclick(value){
	 contrast=value;
	 var img=document.getElementsByTagName('img').item(0);
	 sum();
	 document.getElementById('output').value=contrast;
   }
   function blurclick(value){
	 blurvalue=value;
	 var img=document.getElementsByTagName('img').item(0);
	sum();
	 document.getElementById('output').value=blurvalue;
   }
   function dropshadowclickx(x){
	 dropshadowx=x;
	 sum();
   }
   function dropshadowclicky(y){
	 dropshadowy=y;
	 sum();
   }
   function dropshadowclickl(l){
	 dropshadowl=l;
	 sum();
   }
   function dropshadowclickc(c){
	 dropshadowcolor=c;
	 sum();
   }
   function dropshadowclick(){
	   var img=document.getElementsByTagName('img').item(0);
		img.style.filter="drop-shadow("+dropshadowx+"px "+dropshadowy+"px "+dropshadowl+"px "+dropshadowcolor+")"; 
		
		 }
   function sum(){
	   var img=document.getElementsByTagName('img').item(0);
	   img.style.filter="grayscale("+grayscale+") sepia("+sepia+") saturate("+saturate+")  hue-rotate("+huerotate+"deg) invert("+invert+") opacity("+opacity+") brightness("+brightness+") contrast("+contrast+") blur("+blurvalue+"px) drop-shadow("+dropshadowx+"px "+dropshadowy+"px "+dropshadowl+"px "+dropshadowcolor+")";  
	   img.style.webkitFilter="grayscale("+grayscale+") sepia("+sepia+") saturate("+saturate+")  hue-rotate("+huerotate+"deg) invert("+invert+") opacity("+opacity+") brightness("+brightness+") contrast("+contrast+") blur("+blurvalue+"px) drop-shadow("+dropshadowx+"px "+dropshadowy+"px "+dropshadowl+"px "+dropshadowcolor+")";
	   showvalue();
}
window.onload=function(){
	showvalue();
}
   var $=function(id){
	   return document.getElementById(id);
   }
   function showvalue(){
      $('1').value=grayscale;
	  $('2').value=sepia;
	  $('3').value=saturate;
	  $('4').value=huerotate;
	  $('5').value=invert;
	  $('6').value=opacity;
	  $('7').value=brightness;
	  $('8').value=contrast;
	  $('9').value=blurvalue;
	  $('10').value=dropshadowx;
	  $('11').value=dropshadowy;
	  $('12').value=dropshadowl;
	  $('13').value=dropshadowcolor;
}
function imgExport(){
   	var img=document.getElementById("img");
    var myCanvas=document.getElementById("myCanvas");
    var cxt=myCanvas.getContext("2d");
    cxt.drawImage(img,0,0);
}

function html2canva(){
    html2canvas(document.getElementById('aaa'), {
        onrendered: function(canvas) {
            document.body.appendChild(canvas);
        }
//可以带上宽高截取你所需要的部分内容
//     ,
//     width: 300,
//     height: 300
    });
}