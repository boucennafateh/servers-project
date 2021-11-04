package org.fate7.server.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fate7.server.Enumeration.Status;
import org.fate7.server.model.Server;
import org.fate7.server.repo.ServerRepository;
import org.fate7.server.service.ServerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
        log.info("Saving the server {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        Server serverSaved = serverRepository.save(server);
        return serverSaved;
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging the server IP {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        server.setStatus(inetAddress.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("getting the servers");
        List<Server> servers = serverRepository.findAll(PageRequest.of(0, limit)).toList();
        return servers;
    }

    @Override
    public Server get(Long id) {
        log.info("getting the server {}", id);
        Optional<Server> serverOptional = serverRepository.findById(id);
        return serverOptional.isPresent() ? serverOptional.get() : null;
    }

    @Override
    public Server update(Server server) {
        log.info("updating the server {}", server.getName());
        Server server1 = serverRepository.save(server);
        return server1;
    }

    @Override
    public Boolean delete(Long id) {
        log.info("deleting the server {}", id);
        serverRepository.deleteById(id);
        return true;
    }

    private String setServerImageUrl() {
        String[] serverUrls = {"server1.png","server2.png", "server3.png", "server4.png"};
        int index = new Random().nextInt(4);
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + serverUrls[index]).toUriString();
    }


}
