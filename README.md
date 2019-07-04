# websocket-cluster
websocket-cluster/websocket消息后台推送微服务集群解决方案（spring cloud）
**该服务用于解决在微服务系统中，ws集群后台推送消息**

***部署不同的节点，请修改application.yml 中的 ws.node.name 用于区分节点推送消息***

**客户端通过nginx负载均衡建立ws连接,nginx部分配置如下**

upstream websocket{
        server 192.168.1.202:8774;
        #server 192.168.1.137:8774;
    }

    server {
        listen       80;
        server_name  127.0.0.1;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }
        
        location /ws/ {
            proxy_pass http://websocket/;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
        }
        

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }
    
    
    
   **通过调用接口的方式，去推送ws对点消息时，由于服务是集群方式部署，你调用的服务器并不一定是ws建立连接的服务器**
   **所以在ws连接建立时，将用户身份和所连接的节点信息存储入redis中**
   **每个ws服务器配置好activemq，按节点名配置destination,调用接口时，先查询redis，用户是否在该机器建立连接**
   **如果不是连接ws所在的节点，则发送消息到所在机器中**
