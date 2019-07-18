# FitbitHackaton

Run docker container:
docker run --rm --name pg-docker -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=backenddb -d -p 5432:5432 -v /usr/local/var/postgres:/var/lib/postgresql/data postgres

Connect to psql:
psql -U admin postgresql://localhost:5432/backenddb

See docker running:
docker ps

Docker kill:
docker kill id

Delete stuff:
rm -rf ~/.docker/volumes/postgres
