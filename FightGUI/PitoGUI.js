var milliSecBetweenRound = 1000;
var zoom = 2;
var offsetX = 400;
var offsetY = 400;

$(function () {
    
    
    $('input[name="milliSecBetweenRound"]').val(milliSecBetweenRound);
    $('input[name="zoom"]').val(zoom);
    $('input[name="offsetX"]').val(offsetX);
    $('input[name="offsetY"]').val(offsetY);
    
    $("#changeDisplay").click(function(){
        milliSecBetweenRound = parseInt($('input[name="milliSecBetweenRound"]').val());
        zoom = parseFloat($('input[name="zoom"]').val());
        offsetX = parseFloat($('input[name="offsetX"]').val());
        offsetY = parseFloat($('input[name="offsetY"]').val());
    });
    
    
    var rounds = [];
    var launched = false;
    
    var webSocketClient = new WebSocket('ws://localhost:8080/fight');
    webSocketClient.onmessage = function(e) {
        
        rounds.push(JSON.parse(e.data));
        
        if(!launched){
            launched = true;
            processRound(true);
        }
    };
    webSocketClient.onopen = function(e) { console.log("webSocketClient Connected"); };
    webSocketClient.onclose = function(e) {console.log("webSocketClient Disconnected"); };
    webSocketClient.onerror = function(e) { console.log("webSocketClient Error : " + e); };

    
    function processRound(firstRound){
        
        var round = rounds.shift();
        
        if(round == null) return;
        
        else{
            
            round.forEach(function(vertex){

                //Draw LivingEntities if first round
                if(firstRound)  $("#fight").append(drawLivingEntity(vertex["_2"]));
                //Update with other rounds
                else{
                    var livingEntityContainer = $(`#${vertex["_2"]["id"]}`);
                    var livingEntityCircle = livingEntityContainer.find('div.LivingEntityCircle');
                    var livingEntityText = livingEntityContainer.find('div.text');

                    livingEntityCircle.removeClass("Hurt");
                    livingEntityCircle.html(vertex["_2"]["hp"]);
                    livingEntityText.html(vertex["_2"]["name"]);

                    //Move LivingEntities if not dead
                    if(vertex["_2"]["hp"] > 0){

                        livingEntityContainer.css({left: `${vertex["_2"]["position"]["x"]*zoom + offsetX}px`, top: `${vertex["_2"]["position"]["y"]*zoom + offsetY}px`});
                        if(vertex["_2"]["hurtDuringRound"]) livingEntityCircle.addClass("Hurt");
                    }
                    //Hide LivingEntities
                    else livingEntityContainer.hide();
                }
            });
            
            //Recursive processRound
            setTimeout(function(){
                processRound(false);
            }, milliSecBetweenRound);
            
        }
    }
    
    
    function drawLivingEntity(livingEntity){
        return `<div class="LivingEntityContainer" id = "${livingEntity["id"]}" style="left: ${livingEntity["position"]["x"]*zoom + offsetX}px; top: ${livingEntity["position"]["y"]*zoom + offsetY}px;">
            <div class="${livingEntity["team"]} LivingEntityCircle">${livingEntity["hp"]}</div>
            <div class="text">${livingEntity["name"]}</div>
        </div>`;
    }
    
}); 