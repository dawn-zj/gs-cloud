#!/bin/bash
cd /etc/rc3.d && ./Kserver.sh

sleep 10s

cd /etc/rc3.d && ./Sserver.sh
