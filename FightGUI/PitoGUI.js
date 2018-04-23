var milliSecBetweenRound = 1000;
var zoom = 5;
var offsetX = 0;
var offsetY = 0;

$(function () {
    
    var rounds = [];
    
    //Read round files
    try {
        
        alert("TEST");
        
        var cpt = 1;
        
        while(cpt<2){
            
            alert(cpt);
            
            
            $.getJSON('roundJSON/round'+cpt+'.json', function(parsedJson) {
                rounds.push(parsedJson);
            });
            
            cpt++;
        }
        
    }
    catch(err) {

    } finally{
        
        if(rounds.length > 0) processRound(0);
        else alert("No rounds !");
    }
      
    
    function processRound(numRound){
        
        rounds[numRound].forEach(function(vertex){

            //Draw LivingEntities
            if(numRound==0){

                $("body").append(drawLivingEntity(vertex["_2"]));
            }
            else{
                var livingEntityContainer = $(`#${vertex["_2"]["id"]}`);
                var livingEntityCircle = livingEntityContainer.find('div.LivingEntityCircle');
                var livingEntityText = livingEntityContainer.find('div.text');

                livingEntityText.html( `${vertex["_2"]["name"]} (hp: ${vertex["_2"]["hp"]})` );
                livingEntityCircle.removeClass("Hurt");

                //Move LivingEntities if not dead
                if(vertex["_2"]["hp"] > 0){
                    
                    livingEntityContainer.css({left: `${vertex["_2"]["position"]["x"]*zoom + offsetX}px`, top: `${vertex["_2"]["position"]["y"]*zoom + offsetY}px`});
                    if(vertex["_2"]["hurtDuringRound"]){
                        livingEntityCircle.addClass("Hurt");
                    }
                }
                //Hide LivingEntities
                else livingEntityContainer.hide();
            }
        });
            
        //Recursive processRound
        setTimeout(function(){
            numRound++;
            if(numRound<rounds.length) processRound(numRound);
            else{
                //Clear last round hurt
                $(".Hurt").each(function(){$(this).removeClass("Hurt");});
            }
            
        }, milliSecBetweenRound);
        
    }
    
    
    function drawLivingEntity(livingEntity){
        return `<div class="LivingEntityContainer" id = "${livingEntity["id"]}" style="left: ${livingEntity["position"]["x"]*zoom + offsetX}px; top: ${livingEntity["position"]["y"]*zoom + offsetY}px;">
            <div class="${livingEntity["team"]} LivingEntityCircle"></div>
            <div class="text">${livingEntity["name"]} (hp: ${livingEntity["hp"]})</div>
        </div>`;
    }
    
}); 