<?php

use Ratchet\MessageComponentInterface;
use Ratchet\ConnectionInterface;

require __DIR__."/vendor/autoload.php";

class WSFight implements MessageComponentInterface {
    protected $clients;

    public function __construct() {
        $this->clients = new \SplObjectStorage;
    }

    public function onOpen(ConnectionInterface $conn) {
        echo "Connected";
        $this->clients->attach($conn);
    }

    public function onMessage(ConnectionInterface $from, $msg) {
        foreach ($this->clients as $client) {
            if ($from != $client) $client->send($msg);
        }
    }

    public function onClose(ConnectionInterface $conn) {
        echo "Disconnected";
        $this->clients->detach($conn);
    }

    public function onError(ConnectionInterface $conn, \Exception $e) {
        echo "Error : " + $e;
        $conn->close();
    }
}

// Run the server application through the WebSocket protocol on port 8089
$app = new Ratchet\App('127.0.0.1', 8089);
$app->route('/fight', new WSFight);
$app->run();

?>