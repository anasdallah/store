FROM mongo:4.2.24
COPY roles.json /roles.json
COPY users.json /users.json
COPY items.json /items.json

CMD bash -c 'until mongo --host mongo --eval "print(\"waiting for connection\")"; do sleep 5; done && \
    mongoimport --host mongo --db store --collection roles --type json --jsonArray --file /roles.json --verbose && \
    mongoimport --host mongo --db store --collection users --type json --jsonArray --file /users.json --verbose && \
    mongoimport --host mongo --db store --collection items --type json --jsonArray --file /items.json --verbose'
