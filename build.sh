#!/usr/bin/env bash
docker build . -t icp-liquidator-g
docker run -it  -e ICP_NETWORK='https://ic0.app' \
-e PROTOCOL_PRINCIPAL='difs6-zyaaa-aaaal-aarrq-cai'  \
-e BTC_TOKEN_PRINCIPAL='6ztww-vaaaa-aaaam-qanba-cai' \
-e MINT_TOKEN_PRINCIPAL='jkekj-dqaaa-aaaao-aaf6a-cai' \
-e INIT_APPROVE='false' \
-e AMOUNT_BTC='1000000000000' \
-e AMOUNT_TOKEN='1576000000000000' \
-p 8080:8080 -t icp-liquidator-g
