package org.fate7.server.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.fate7.server.model.Server;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {

    Server create(Server server);
    Collection<Server> list(int limit);
    Server get(Long id);
    Server update(Server server);
    Boolean delete(Long id);
    Server ping(String ipAddress) throws IOException;

}
