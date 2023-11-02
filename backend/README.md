# Multiplatform Kickstarter Service Template
Basic Service template in ktor


## How to Run this service
```
./gradlew run
```

## How to run it using Docker
```
docker build -t myprojectname .
docker run -p 8080:8080 myprojectname
```

To run MK Service container as a service on Linux with systemd:﻿
Create a service descriptor file /etc/systemd/system/docker.myprojectname.service:

```
[Unit]
Description=MK Service
After=docker.service
Requires=docker.service

[Service]
TimeoutStartSec=0
Restart=always
ExecStartPre=-/usr/bin/docker exec %n stop
ExecStartPre=-/usr/bin/docker rm %n
ExecStart=/usr/bin/docker run -p 8080:8080 myprojectname

[Install]
WantedBy=default.target
```


Enable starting the service on system boot with the following command:
```
sudo systemctl enable docker.myprojectname
```


You can also stop and start the service manually at any moment with the following commands, respectively:
```
sudo service docker.myprojectname stop
sudo service docker.myprojectname start
```
