var milliSecBetweenRound = 1000;
var zoom = 2;
var offsetX = 400;
var offsetY = 400;
var minMilliSecBetweenRound = 500;

$(function () {
    
    //----------------------
    // Inputs form
    //----------------------
    
    $('input[name="milliSecBetweenRound"]').val(milliSecBetweenRound);
    $('input[name="zoom"]').val(zoom);
    $('input[name="offsetX"]').val(offsetX);
    $('input[name="offsetY"]').val(offsetY);
    
    $('input[name="milliSecBetweenRound"]').change(function(){
        var newVal = parseInt($('input[name="milliSecBetweenRound"]').val());
        //Be sure to have milliSecBetweenRound > time for the Scala program to stream one round
        if(newVal < minMilliSecBetweenRound){
            newVal = minMilliSecBetweenRound;
            $(this).val(newVal);
        }
        milliSecBetweenRound = newVal;
    });
    
    $('input[name="zoom"]').change(function(){
        zoom = parseFloat($('input[name="zoom"]').val());
    });
    
    $('input[name="offsetX"]').change(function(){
        offsetX = parseInt($('input[name="offsetX"]').val());
    });
    
    $('input[name="offsetY"]').change(function(){
        offsetY = parseInt($('input[name="offsetY"]').val());
    });

    /* input label style */

    $(".input-effect input").each(function() {
        $(this).addClass("has-content");
    });

    $(".input-effect input").focusout(function() {
        if($(this).val() != "") $(this).addClass("has-content");
        else $(this).removeClass("has-content");
    });


    //----------------------
    // Rounds variables
    //----------------------
    
    var rounds = [];
    var isFightLaunched = false;
    
    //----------------------
    // WebSocket Client
    //----------------------
    
    var webSocketClient = new WebSocket('ws://localhost:8089/fight');
    webSocketClient.onmessage = function(e) {
        //If Scala program begins, clear fight
        if(e.data == "FightBeginning"){
            rounds = [];
            isFightLaunched = false;
            $("#fight").html("");
        }
        //Else Scala program is sending data
        else{
            
            rounds.push(JSON.parse(e.data));

            //First round
            if(!isFightLaunched){
                isFightLaunched = true;
                processRound(true);
            }
        }
    };
    webSocketClient.onopen = function(e) { console.log("webSocketClient Connected"); };
    webSocketClient.onclose = function(e) {console.log("webSocketClient Disconnected"); };
    webSocketClient.onerror = function(e) { console.log("webSocketClient Error : " + e); };
    
    //----------------------
    // Proceed one round
    //----------------------
    
    function processRound(firstRound){
        
        var round = rounds.shift();
        
        //If there is not more round to proceed
        if(round == null){
            //Clear hurt entities
            $(".Hurt").each(function(){
                $(this).removeClass("Hurt");
            });
            
            return;
        }
        
        else{
            
            //Clear area if first round
            //if(firstRound) $("#fight").html("");
            
            round.forEach(function(vertex){

                //Draw LivingEntity if first round
                if(firstRound) $("#fight").append(drawLivingEntity(vertex["_2"]));
                //Other rounds -> update
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
    
    //----------------------
    // Drawn one LivingEntity
    //----------------------
    
    function drawLivingEntity(livingEntity){
        var str = `<div class="LivingEntityContainer" id = "${livingEntity["id"]}" style="left: ${livingEntity["position"]["x"]*zoom + offsetX}px; top: ${livingEntity["position"]["y"]*zoom + offsetY}px; z-index: `;
        
        if(livingEntity["team"] == "BadGuys") str += '2';
        else str += '3';
        
        str += `;">
            <div class="${livingEntity["team"]} LivingEntityCircle">${livingEntity["hp"]}</div>
            <div class="text">${livingEntity["name"]}</div>
        </div>`;
        
        return str;
    }
    
}); 