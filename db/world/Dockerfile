# Use the latest MySQL image
FROM mysql


COPY world.sql /docker-entrypoint-initdb.d
# Set the root password
ENV MYSQL_ROOT_PASSWORD w0rldDBp@ss