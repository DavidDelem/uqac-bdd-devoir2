var milliSecBetweenRound = 1000;
var nbRoundMax = 100;
var zoom = 2;
var offsetX = 400;
var offsetY = 400;

$(function () {
    
    var rounds = [];

		
		var cpt = 1;
		var launched = false;
		
		while(cpt<nbRoundMax){

				$.getJSON('roundJSON/round'+cpt+'.json'/*, function(parsedJson) {
						alert("TEST");
						rounds.push(parsedJson);
				}*/).done(function(data) {
						rounds.push(data);
				}).error(function(error) {
						if(rounds.length > 0 && !launched){
							launched = true;
							processRound(0);
						}
				});

				cpt++;
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