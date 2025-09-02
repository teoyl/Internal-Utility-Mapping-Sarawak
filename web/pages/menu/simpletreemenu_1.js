function treeMenu(treeid, enablepersist, persistdays, nodeTextClicked, closeImage, openImage)
{
  var persisteduls=new Object()
  var ddtreemenu= this;
  var count = 9000;
  
  ddtreemenu.nodeTextClicked = nodeTextClicked;
  ddtreemenu.selectedNodeId = ""
  ddtreemenu.treeid = treeid
  ddtreemenu.closefolder = closeImage
  ddtreemenu.openfolder = openImage
  ddtreemenu.enablepersist = enablepersist
  ddtreemenu.persistdays = persistdays
  
  /**------------------------------ 1. IE  ------------------------------**/
  ddtreemenu.createTreeIE = function()
  {
    var ultags=document.getElementById(treeid).getElementsByTagName("ul")
    if (typeof persisteduls[treeid]=="undefined")
    persisteduls[treeid]=(ddtreemenu.enablepersist==true && ddtreemenu.getCookie(treeid)!="")? ddtreemenu.getCookie(treeid).split(",") : ""
    
    //Process all leafs  
    var litags=document.getElementById(treeid).getElementsByTagName("li")
    for (var j=0; j<litags.length; j++){
      if(litags[j].className == "leaf"){
        //litags[j].getElementsByTagName("span")[1].attachEvent('onclick',ddtreemenu.setSelectedIE)
        litags[j].getElementsByTagName("span")[0].firstChild.attachEvent('onclick',ddtreemenu.setSelectedIE)
        
        //if(ddtreemenu.nodeTextClicked != "") litags[j].getElementsByTagName("span")[1].attachEvent("onclick",ddtreemenu.nodeTextClicked);
        if(ddtreemenu.nodeTextClicked != "") litags[j].getElementsByTagName("span")[0].firstChild.attachEvent("onclick",ddtreemenu.nodeTextClicked);
      }
    }
    
    //Process all branches  
    for (var i=0; i<ultags.length; i++)
    {
      ddtreemenu.buildSubTreeIE(treeid, ultags[i], i)
    }
    
    if (ddtreemenu.enablepersist==true){ //if enable persist feature
      var durationdays=(typeof ddtreemenu.persistdays=="undefined")? 1 : parseInt(ddtreemenu.persistdays)
      ddtreemenu.dotask(window, function(){ddtreemenu.rememberstate(treeid, durationdays)}, "unload") //save opened UL indexes on body unload
    }
  }
  
  ddtreemenu.buildSubTreeIE=function(treeid, ulelement, index){
    if (typeof persisteduls[treeid]=="object"){ //if cookie exists (persisteduls[treeid] is an array versus "" string)
    
      if (ddtreemenu.searcharray(persisteduls[treeid], index)){
        ulelement.setAttribute("rel", "open")
        ulelement.style.display="block"
        ulelement.parentNode.getElementsByTagName("span")[0].style.backgroundImage="url("+ddtreemenu.openfolder+")"
        
      }
      else
      ulelement.setAttribute("rel", "closed")
      
    } //end cookie persist code
    else if (ulelement.getAttribute("rel")== null || ulelement.getAttribute("rel")==false) //if no cookie and UL has NO rel attribute explicted added by user
      ulelement.setAttribute("rel", "closed")
    else if (ulelement.getAttribute("rel")=="open") //else if no cookie and this UL has an explicit rel value of "open"
    
    ddtreemenu.expandSubTree(treeid, ulelement) //expand this UL plus all parent ULs (so the most inner UL is revealed!)
    
    var x = ulelement.parentNode.getElementsByTagName("span")[0];
    
    //Set listener for Branch Node
    ulelement.parentNode.getElementsByTagName("span")[0].attachEvent("onclick",ddtreemenu.toggleIE);
    ulelement.parentNode.getElementsByTagName("span")[0].firstChild.attachEvent("onclick",ddtreemenu.setSelectedIE);
    //ulelement.parentNode.getElementsByTagName("span")[0].firstChild.attachEvent("onclick",ddtreemenu.toggleBranchIE);
    
    if(ddtreemenu.nodeTextClicked != "" ) {
      ulelement.parentNode.getElementsByTagName("span")[0].firstChild.attachEvent("onclick",ddtreemenu.nodeTextClicked);
    }else{
      ulelement.parentNode.getElementsByTagName("span")[0].firstChild.attachEvent("onclick",ddtreemenu.toggleBranchIE);
    }
    
    ulelement.parentNode.getElementsByTagName("span")[0].firstChild.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    
    //ulelement.parentNode.getElementsByTagName("span")[1].attachEvent("onclick",ddtreemenu.setSelectedIE);
    
    //if(ddtreemenu.nodeTextClicked != "" ) ulelement.parentNode.getElementsByTagName("span")[1].attachEvent("onclick",ddtreemenu.nodeTextClicked);
    
    ulelement.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
  }
  
  ddtreemenu.toggleIE=function(){ //expand or contract the selected Node
  
    if (window.event) {
      target = window.event.srcElement;
    } else if (e) {
      target = e.target;
    } 
    
    if (target.nodeName == 'A') 
    {
      var submenu=target.parentNode.parentNode.parentNode.getElementsByTagName("ul")[0]
      var className = target.parentNode.parentNode.parentNode.getElementsByTagName("div")[0].className
    }
    else
    {
      var submenu=target.parentNode.parentNode.getElementsByTagName("ul")[0]
      var className = target.parentNode.parentNode.getElementsByTagName("div")[0].className
    }
    
    var temp = submenu.getAttribute("rel")
    ddtreemenu.flatten(ddtreemenu.treeid, "flatten") //Close all other menu
    
    if (temp=="closed"){
      ddtreemenu.expandSubTree(ddtreemenu.treeid, submenu)  //Expand the parent if submenu is toggled.
      submenu.style.display="block"
      submenu.setAttribute("rel", "open")
      target.style.backgroundImage="url("+ddtreemenu.openfolder+")"
      target.style.backgroundRepeat="no-repeat"
    }
    else if (temp=="open"){
      if( className == '')
      {
        ddtreemenu.expandSubTree(ddtreemenu.treeid, submenu)  //Expand the parent if submenu is toggled.
      }
      submenu.style.display="none"
      submenu.setAttribute("rel", "closed")
      target.style.backgroundImage="url("+ddtreemenu.closefolder+")"
      target.style.backgroundRepeat="no-repeat"
    }
  }
  
  ddtreemenu.toggleBranchIE=function(){ //Branch Node allow expand only
  
    if (window.event) {
      target = window.event.srcElement;
    } else if (e) {
      target = e.target;
    } 
    
    if (target.nodeName == 'A') 
    {
      var submenu=target.parentNode.parentNode.parentNode.getElementsByTagName("ul")[0]
      var className = target.parentNode.parentNode.parentNode.getElementsByTagName("div")[0].className
    }
    else
    {
      var submenu=target.parentNode.parentNode.getElementsByTagName("ul")[0]
      var className = target.parentNode.parentNode.getElementsByTagName("div")[0].className
    }
    
    var temp = submenu.getAttribute("rel")
    ddtreemenu.flatten(ddtreemenu.treeid, "flatten") //Close all other menu
    
   
    ddtreemenu.expandSubTree(ddtreemenu.treeid, submenu) 
    submenu.style.display="block"
    submenu.setAttribute("rel", "open")
    //target.style.backgroundImage="url("+ddtreemenu.openfolder+")"
    //target.style.backgroundRepeat="no-repeat"
    
  }
  
  ddtreemenu.setSelectedIE=function()
  {
    if (window.event) {
      target = window.event.srcElement;
    } else if (e) {
      target = e.target;
    } 
    
    if(ddtreemenu.selectedNodeId != target.parentNode.parentNode.parentNode.id){
      if (ddtreemenu.selectedNodeId != ""){
        //document.getElementById(ddtreemenu.selectedNodeId).getElementsByTagName("span")[1].className = "";
        document.getElementById(ddtreemenu.selectedNodeId).getElementsByTagName("span")[0].firstChild.className = "";
        var temp_obj = document.getElementById(ddtreemenu.selectedNodeId);
        if (temp_obj != null){
          if(temp_obj.className.indexOf("toplevel") >= 0){
            temp_obj.className = "submenu toplevel"
          }else if(temp_obj.className.indexOf("submenu") >= 0){
            temp_obj.className = "submenu"
          }else{
            temp_obj.className = "leaf"
          }
        }        
      }
      ddtreemenu.selectedNodeId = target.parentNode.parentNode.parentNode.id;   
      document.getElementById(ddtreemenu.selectedNodeId).className = document.getElementById(ddtreemenu.selectedNodeId).className + " selected";    
      document.getElementById(ddtreemenu.selectedNodeId).getElementsByTagName("span")[0].firstChild.className = "select";
    }
  }
  
  ddtreemenu.addChildFF = function() {
    var node = document.getElementById(ddtreemenu.selectedNodeId);
   
    
    count = count + 1;
  }
  
  ddtreemenu.addChildIE = function()
  {
    var node = document.getElementById(ddtreemenu.selectedNodeId);
 
    var li1 = document.createElement("li")
    li1.setAttribute("id", "[NEW]" + count)
    li1.className = "leaf"
    
    var div1 = document.createElement("div")
    
    var span1 = document.createElement("span")
    span1.setAttribute("class", "imgLeaf")
    span1.className="imgLeaf"
    
    var objA = document.createElement("a")
    objA.innerHTML = "[NEW]"
    objA.attachEvent("onclick",ddtreemenu.setSelectedIE);
    if(ddtreemenu.nodeTextClicked != "" ) objA.attachEvent("onclick",ddtreemenu.nodeTextClicked);
    
    span1.appendChild(objA)
    div1.appendChild(span1)
    li1.appendChild(div1)
    node.parentNode.appendChild(li1);
    
    var ul1 = document.createElement("ul")
    ul1.setAttribute("style","display: none;")
    ul1.setAttribute("rel", "closed")
    ul1.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    ul1.appendChild(li1)
    
    node.appendChild(ul1)
    
    node.className = "submenu"
    node.getElementsByTagName("span")[0].setAttribute("style", "background-image: url(images/menu_plusminus_close.gif); background-repeat: no-repeat;")
    node.getElementsByTagName("span")[0].className = "img"
    
    
    node.getElementsByTagName("span")[0].attachEvent("onclick",ddtreemenu.toggleIE);
    node.getElementsByTagName("span")[0].firstChild.attachEvent("onclick",ddtreemenu.setSelectedIE);
 if(ddtreemenu.nodeTextClicked != "" ) node.getElementsByTagName("span")[0].firstChild.attachEvent("onclick",ddtreemenu.nodeTextClicked);
    
    node.getElementsByTagName("span")[0].firstChild.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    
    //Select the newly created Node
    //span1.firstChild.fireEvent("onclick");
    
    count = count + 1;
  }
  
  
  ddtreemenu.addIE = function()
  {
    var node = document.getElementById(ddtreemenu.selectedNodeId);
 
    var li1 = document.createElement("li")
    li1.setAttribute("id", "[NEW]" + count)
    li1.className = "leaf"
    
    var div1 = document.createElement("div")
    
    var span1 = document.createElement("span")
    span1.setAttribute("class", "imgLeaf")
    span1.className="imgLeaf"
    
    var objA = document.createElement("a")
    objA.innerHTML = "[NEW]"
    objA.attachEvent("onclick",ddtreemenu.setSelectedIE);
    if(ddtreemenu.nodeTextClicked != "" ) objA.attachEvent("onclick",ddtreemenu.nodeTextClicked);
    span1.appendChild(objA)
    
    div1.appendChild(span1)
    //div1.appendChild(span2)
    li1.appendChild(div1)
    node.parentNode.appendChild(li1);
    
    //Select the newly created Node
    span1.firstChild.fireEvent("onclick");
    
    count = count + 1;
  }
  
  /**------------------------------ 2. FF  ------------------------------**/ 
  ddtreemenu.createTreeFF = function(){    
    var ultags=document.getElementById(treeid).getElementsByTagName("ul")
    //Check if persist is defined.
    if (typeof persisteduls[treeid]=="undefined")
    persisteduls[treeid]=(ddtreemenu.enablepersist==true && ddtreemenu.getCookie(treeid)!="")? ddtreemenu.getCookie(treeid).split(",") : ""
    
    //Process all the Leafs  
    var litags=document.getElementById(treeid).getElementsByTagName("li")
    for (var j=0; j<litags.length; j++){
      if(litags[j].className == "leaf"){
        //litags[j].getElementsByTagName("span")[1].addEventListener("click",ddtreemenu.setSelected,true);
        litags[j].getElementsByTagName("span")[0].firstChild.addEventListener("click",ddtreemenu.setSelected,true);
        
        //if(ddtreemenu.nodeTextClicked != "") litags[j].getElementsByTagName("span")[1].addEventListener("click",ddtreemenu.nodeTextClicked,true);
        if(ddtreemenu.nodeTextClicked != "") litags[j].getElementsByTagName("span")[0].firstChild.addEventListener("click",ddtreemenu.nodeTextClicked,true);
      }
    }
    
    //Process all branches  
    for (var i=0; i<ultags.length; i++)
    {
      ddtreemenu.buildSubTree(treeid, ultags[i], i)
    }
    
    if (ddtreemenu.enablepersist==true){ //if enable persist feature
      var durationdays=(typeof ddtreemenu.persistdays=="undefined")? 1 : parseInt(ddtreemenu.persistdays)
      ddtreemenu.dotask(window, function(){ddtreemenu.rememberstate(treeid, durationdays)}, "unload") //save opened UL indexes on body unload
    }
  }
  
  ddtreemenu.addChildFF = function() {
    var node = document.getElementById(ddtreemenu.selectedNodeId);
      
    //New node
    var li1 = document.createElement("li")
    li1.setAttribute("id", "[NEW]" + count)
    li1.setAttribute("class", "leaf")
    
    var div1 = document.createElement("div")
    
    var span1 = document.createElement("span")
    span1.setAttribute("class", "imgLeaf")
    
    var objA = document.createElement("a")
    objA.innerHTML = "[NEW]"
    objA.addEventListener("click",ddtreemenu.setSelected,false);
    if(ddtreemenu.nodeTextClicked != "" ) objA.addEventListener("click",ddtreemenu.nodeTextClicked,false);
    
    span1.appendChild(objA)
    div1.appendChild(span1)
    li1.appendChild(div1)
    
    var ul1 = document.createElement("ul")
    ul1.setAttribute("style","display: none;")
    ul1.setAttribute("rel", "closed")
    ul1.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    ul1.appendChild(li1)
    
    node.appendChild(ul1)
    
    node.setAttribute("class", "submenu")
    node.getElementsByTagName("span")[0].setAttribute("style", "background-image: url(images/menu_plusminus_close.gif); background-repeat: no-repeat;")
    node.getElementsByTagName("span")[0].setAttribute("class", "img")
    node.getElementsByTagName("span")[0].addEventListener("click",ddtreemenu.toggle,false);
    node.getElementsByTagName("span")[0].onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    node.getElementsByTagName("span")[0].firstChild.addEventListener("click",ddtreemenu.setSelected,false);
    if(ddtreemenu.nodeTextClicked != "" ) node.getElementsByTagName("span")[0].firstChild.addEventListener("click",ddtreemenu.nodeTextClicked,false);
    
    node.getElementsByTagName("span")[0].firstChild.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    
    count = count + 1;
  }
  
  ddtreemenu.addFF = function() {
    var node = document.getElementById(ddtreemenu.selectedNodeId);
    
    //New node
    var li1 = document.createElement("li")
    li1.setAttribute("id", "[NEW]" + count)
    li1.setAttribute("class", "leaf")
    
    var div1 = document.createElement("div")
    
    var span1 = document.createElement("span")
    span1.setAttribute("class", "imgLeaf")
    
    var objA = document.createElement("a")
    objA.innerHTML = "[NEW]"
    objA.addEventListener("click",ddtreemenu.setSelected,false);
    if(ddtreemenu.nodeTextClicked != "" ) objA.addEventListener("click",ddtreemenu.nodeTextClicked,false);
    
    span1.appendChild(objA)
    div1.appendChild(span1)
    li1.appendChild(div1)
    /*
    var ul1 = document.createElement("ul")
    ul1.setAttribute("style","display: none;")
    ul1.setAttribute("rel", "closed")
    
    ul1.appendChild(li1)
    
    count = count + 1
    
    //Submenu
    var li2 = document.createElement("li")
    li2.setAttribute("id", "[NEW]" + count)
    li2.setAttribute("class", "submenu")
    
    var div2 = document.createElement("div")
    
    var span2 = document.createElement("span")
    span2.setAttribute("class", "img")
    span2.setAttribute("style", "background-image: url(images/menu_plusminus_close.gif);")
    
    span2.addEventListener("click",ddtreemenu.toggle,false);
    span2.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    
    
    var objA2 = document.createElement("a")
    objA2.innerHTML = "[NEW]"
    objA2.setAttribute("class", "")
    objA2.addEventListener("click",ddtreemenu.setSelected,true);
    if(ddtreemenu.nodeTextClicked != "" ) objA2.addEventListener("click",ddtreemenu.nodeTextClicked,true);
    
    
    span2.appendChild(objA2)
    div2.appendChild(span2)
    li2.appendChild(div2)
    li2.appendChild(ul1)
    */
    node.parentNode.appendChild(li1);
    
    /*
    var li1 = document.createElement("li")
    li1.setAttribute("id", "[NEW]" + count)
    li1.setAttribute("class", "leaf")
    
    var div1 = document.createElement("div")
    
    var span1 = document.createElement("span")
    span1.setAttribute("class", "imgLeaf")
    
    var span2 = document.createElement("span")
    var objA = document.createElement("a")
    objA.innerHTML = "[NEW]"
    objA.addEventListener("click",ddtreemenu.setSelected,true);
    if(ddtreemenu.nodeTextClicked != "" ) objA.addEventListener("click",ddtreemenu.nodeTextClicked,true);
    span1.appendChild(objA)
    
    div1.appendChild(span1)
    //div1.appendChild(span2)
    li1.appendChild(div1)
    node.parentNode.appendChild(li1);
    */
    
    //Select the newly created Node
    
    var evt = document.createEvent("MouseEvents");
    evt.initMouseEvent("click", true, true, window,
      0, 0, 0, 0, 0, false, false, false, false, 0, null);
    span1.firstChild.dispatchEvent(evt);
    
    /*
    var evt = document.createEvent("MouseEvents");
    evt.initMouseEvent("click", true, true, window,
      0, 0, 0, 0, 0, false, false, false, false, 0, null);
    span2.dispatchEvent(evt);
    */
    
    count = count + 1;
  }
  
  //The function called when user clicked on a node
  ddtreemenu.setSelected=function() {
     
     if(ddtreemenu.selectedNodeId != this.parentNode.parentNode.parentNode.id){
      
      if (ddtreemenu.selectedNodeId != ""){
        
        //document.getElementById(ddtreemenu.selectedNodeId).getElementsByTagName("span")[1].className = "";
        document.getElementById(ddtreemenu.selectedNodeId).getElementsByTagName("span")[0].firstChild.className = "";
        var temp_obj = document.getElementById(ddtreemenu.selectedNodeId);
        
        if (temp_obj != null){
          
          if(temp_obj.className.indexOf("toplevel") >= 0){
            temp_obj.className = "submenu toplevel"
          }else if(temp_obj.className.indexOf("submenu") >= 0){
            temp_obj.className = "submenu"
          }else{
            temp_obj.className = "leaf"
          }
        }        
        
      }
      ddtreemenu.selectedNodeId = this.parentNode.parentNode.parentNode.id;   
      document.getElementById(ddtreemenu.selectedNodeId).className = document.getElementById(ddtreemenu.selectedNodeId).className + " selected";    
    }
  }
  
  ddtreemenu.getSelected=function() {
    return ddtreemenu.selectedNodeId;
  }
  
  ddtreemenu.buildSubTree=function(treeid, ulelement, index){
    //ulelement.parentNode.className="submenu"
    if (typeof persisteduls[treeid]=="object"){ //if cookie exists (persisteduls[treeid] is an array versus "" string)
      
      if (ddtreemenu.searcharray(persisteduls[treeid], index)){
        ulelement.setAttribute("rel", "open")
        ulelement.style.display="block"
        //ulelement.parentNode.style.backgroundImage="url("+ddtreemenu.openfolder+")"
        ulelement.parentNode.getElementsByTagName("span")[0].style.backgroundImage="url("+ddtreemenu.openfolder+")"
      }
      else
      ulelement.setAttribute("rel", "closed")
      
    } //end cookie persist code
    else if (ulelement.getAttribute("rel")==null || ulelement.getAttribute("rel")==false) //if no cookie and UL has NO rel attribute explicted added by user
      ulelement.setAttribute("rel", "closed")
    else if (ulelement.getAttribute("rel")=="open") //else if no cookie and this UL has an explicit rel value of "open"
    
    ddtreemenu.expandSubTree(treeid, ulelement) //expand this UL plus all parent ULs (so the most inner UL is revealed!)
    
    var x = ulelement.parentNode.getElementsByTagName("span")[0];
    
   
    
    //Set listener for Branch Node
 
    ulelement.parentNode.getElementsByTagName("span")[0].addEventListener("click",ddtreemenu.toggle,false);
    ulelement.parentNode.getElementsByTagName("span")[0].onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    
    ulelement.parentNode.getElementsByTagName("span")[0].firstChild.addEventListener("click",ddtreemenu.setSelected,false);
   
    //ulelement.parentNode.getElementsByTagName("span")[1].addEventListener("click",ddtreemenu.setSelected,true);
    
    //if(ddtreemenu.nodeTextClicked != "" ) ulelement.parentNode.getElementsByTagName("span")[1].addEventListener("click",ddtreemenu.nodeTextClicked,true);
    if(ddtreemenu.nodeTextClicked != "" ) {
      ulelement.parentNode.getElementsByTagName("span")[0].firstChild.addEventListener("click",ddtreemenu.nodeTextClicked,false);
    }else{
      ulelement.parentNode.getElementsByTagName("span")[0].firstChild.addEventListener("click",ddtreemenu.toggleBranch,false);
    }
    
    ulelement.parentNode.getElementsByTagName("span")[0].firstChild.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
    
    ulelement.onclick=function(e){
      ddtreemenu.preventpropagate(e)
    }
  }
  
  ddtreemenu.expandSubTree=function(treeid, ulelement){ //expand a UL element and any of its parent ULs
    var rootnode=document.getElementById(treeid)
    var currentnode=ulelement
    currentnode.style.display="block"
    currentnode.parentNode.getElementsByTagName("span")[0].style.backgroundImage="url("+ddtreemenu.openfolder+")" //Change the Image
    while (currentnode!=rootnode){
      if (currentnode.tagName=="UL"){ //if parent node is a UL, expand it too
        currentnode.style.display="block"
        currentnode.setAttribute("rel", "open") //indicate it's open
        currentnode.parentNode.getElementsByTagName("span")[0].style.backgroundImage="url("+ddtreemenu.openfolder+")" //Change the Image
      }
      currentnode=currentnode.parentNode
    }
  }
  
  ddtreemenu.flatten=function(treeid, action){ //expand or contract all UL elements
    var ultags=document.getElementById(treeid).getElementsByTagName("ul")
    
    for (var i=0; i<ultags.length; i++){
      ultags[i].style.display=(action=="expand")? "block" : "none"
      var relvalue=(action=="expand")? "open" : "closed"
      ultags[i].setAttribute("rel", relvalue)
      ultags[i].parentNode.getElementsByTagName("span")[0].style.backgroundImage=(action=="expand")? "url("+ddtreemenu.openfolder+")" : "url("+ddtreemenu.closefolder+")"
    }
  }
  
  ddtreemenu.selectedNode=function(){ //Display selectedNode
    alert(ddtreemenu.selectedNodeId);
  }
  
  ddtreemenu.toggle=function(){ //expand or contract the selected Node

    var submenu=this.parentNode.parentNode.getElementsByTagName("ul")[0]
    var className = this.parentNode.parentNode.getElementsByTagName("div")[0].className
    var temp = submenu.getAttribute("rel")

    ddtreemenu.flatten(ddtreemenu.treeid, "flatten") //Close all other menu
   
    if (temp =="closed"){
      ddtreemenu.expandSubTree(ddtreemenu.treeid, submenu)  //Expand the parent if submenu is toggled.
      submenu.style.display="block"
      submenu.setAttribute("rel", "open")
      this.style.backgroundImage="url("+ddtreemenu.openfolder+")"
      this.style.backgroundRepeat="no-repeat"
    }
    else if (temp=="open"){
      if(className != "toplevel"){
        ddtreemenu.expandSubTree(ddtreemenu.treeid, submenu)  //Expand the parent if submenu is toggled.
      } 
      submenu.style.display="none"
      submenu.setAttribute("rel", "closed")
      this.style.backgroundImage="url("+ddtreemenu.closefolder+")"
      this.style.backgroundRepeat="no-repeat"
      
      
    }
  
  }
  
  ddtreemenu.toggleBranch=function(){ //expand or contract the selected Node
    
  
    //alert("toggleBranch" + this.nodeName)
    
    var submenu=this.parentNode.parentNode.parentNode.getElementsByTagName("ul")[0]
    var className = this.parentNode.parentNode.parentNode.getElementsByTagName("div")[0].className
    var temp = submenu.getAttribute("rel")

    ddtreemenu.flatten(ddtreemenu.treeid, "flatten") //Close all other menu
   
    ddtreemenu.expandSubTree(ddtreemenu.treeid, submenu)  //Expand the parent if submenu is toggled.
    submenu.style.display="block"
    submenu.setAttribute("rel", "open")
    //this.style.backgroundImage="url("+ddtreemenu.openfolder+")"
    //this.style.backgroundRepeat="no-repeat"
 
  }
  
  ddtreemenu.rememberstate=function(treeid, durationdays){ //store index of opened ULs relative to other ULs in Tree into cookie
    var ultags=document.getElementById(treeid).getElementsByTagName("ul")
    var openuls=new Array()
    for (var i=0; i<ultags.length; i++){
    
      if (ultags[i].getAttribute("rel")=="open"){
        openuls[openuls.length]=i //save the index of the opened UL (relative to the entire list of ULs) as an array element
      }
    }
    
    if (openuls.length==0) { //if there are no opened ULs to save/persist
      openuls[0]="none open" //set array value to string to simply indicate all ULs should persist with state being closed
    }
    ddtreemenu.setCookie(treeid, openuls.join(","), durationdays) //populate cookie with value treeid=1,2,3 etc (where 1,2... are the indexes of the opened ULs)
  }
  
  ////A few utility functions below//////////////////////
  
  ddtreemenu.getCookie=function(Name){ //get cookie value
    var re=new RegExp(Name+"=[^;]+", "i"); //construct RE to search for target name/value pair
    if (document.cookie.match(re)) //if cookie found
    return document.cookie.match(re)[0].split("=")[1] //return its value
    return ""
  }
  
  ddtreemenu.setCookie=function(name, value, days){ //set cookei value
    var expireDate = new Date()
    //set "expstring" to either future or past date, to set or delete cookie, respectively
    var expstring
    
    expstring = expireDate.setDate(expireDate.getDate()+parseInt(days))   
    if (days != '0')
    {
      document.cookie = name+"="+value+"; expires="+expireDate.toGMTString()+"; path=/";
    }
    else
    {
      document.cookie = name+"="+value+"; expires=\"\"; path=/";
    }
  }
  
  ddtreemenu.searcharray=function(thearray, value){ //searches an array for the entered value. If found, delete value from array
    var isfound=false
    for (var i=0; i<thearray.length; i++){
      if (thearray[i]==value){
        isfound=true
        thearray.shift() //delete this element from array for efficiency sake
        break
      }
    }
    return isfound
  }
  
  ddtreemenu.preventpropagate=function(e){ //prevent action from bubbling upwards
    if (typeof e!="undefined"){
      e.stopPropagation()
    }else{
      event.cancelBubble=true
    }
  }
  
  ddtreemenu.dotask=function(target, functionref, tasktype){ //assign a function to execute to an event handler (ie: onunload)
    var tasktype=(window.addEventListener)? tasktype : "on"+tasktype
    
    if (target.addEventListener){
      target.addEventListener(tasktype, functionref, false)
    }else if (target.attachEvent){
      target.attachEvent(tasktype, functionref)
    }
  }
  
  ddtreemenu.deleteNode = function(){
    var node = document.getElementById(ddtreemenu.selectedNodeId)
    node.parentNode.removeChild(node)
    ddtreemenu.selectedNodeId = ""
  }
  
  ddtreemenu.addChild = function() {
    if (document.all && document.getElementById && !window.opera) ddtreemenu.addChildIE();
    if (!document.all && document.getElementById && !window.opera) ddtreemenu.addChildFF();
  }
  
  ddtreemenu.add = function() {
    if (document.all && document.getElementById && !window.opera) ddtreemenu.addIE();
    if (!document.all && document.getElementById && !window.opera) ddtreemenu.addFF();
  }
  
  if (document.all && document.getElementById && !window.opera) ddtreemenu.createTreeIE();
	if (!document.all && document.getElementById && !window.opera) ddtreemenu.createTreeFF();
  
}