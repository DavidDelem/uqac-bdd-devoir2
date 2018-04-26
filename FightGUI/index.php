<?php

shell_exec('php WebSocketServer.php >> ' + sys_get_temp_dir());


echo '<!doctype html>
<html lang="fr">
    <head>
        <meta charset="utf-8">
        <title>Pito GUI</title>
        <link rel="stylesheet" type="text/css" href="PitoGUI.css">
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,500|Roboto:100,300,400,500" rel="stylesheet">
    </head>
    <body>
        
        <div id="params">
            <h1>Display parameters</h1>
            <div class="col-3 input-effect">
                <input class="effect-16" type="text" name="milliSecBetweenRound" value="" placeholder="">
                <label>milliSecBetweenRound</label>
                <span class="focus-border"></span>
            </div>
            <div class="col-3 input-effect">
                <input class="effect-16" type="text" name="zoom" value="" placeholder="">
                <label>zoom</label>
                <span class="focus-border"></span>
            </div>
            <div class="col-3 input-effect">
                <input class="effect-16" type="text" name="offsetX" value="" placeholder="">
                <label>offsetX</label>
                <span class="focus-border"></span>
            </div>
            <div class="col-3 input-effect">
                <input class="effect-16" type="text" name="offsetY" value="" placeholder="">
                <label>offsetY</label>
                <span class="focus-border"></span>
            </div>
        </div>
        <div id="fightContainer">
            <div id="fight"></div>
        </div>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
        <script src="PitoGUI.js"></script>
    </body>
</html>';

?>