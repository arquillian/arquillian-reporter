package org.arquillian.environment.reporter.impl;

import java.io.File;

public class DockerDetector {

    static final String DOCKER_SOCK = "docker.sock";
    static final String DOCKERINIT  = ".dockerinit";
    static final String DOCKERENV   = ".dockerenv";

    static final String ROOT_DOCKER_FILE   = "/";
    static final String ROOT_DOCKER_SOCKET = "/var/run/";

    static final File DOCKER_ENV_PATH = new File(ROOT_DOCKER_FILE, DOCKERENV);
    static final File DOCKER_INIT_PATH = new File(ROOT_DOCKER_FILE, DOCKERINIT);
    static final File DOCKER_SOCKET_FILE = new File(ROOT_DOCKER_SOCKET, DOCKER_SOCK);

    private DockerDetector() {
        super();
    }

    public static boolean detect() {
        return DOCKER_ENV_PATH.exists() && DOCKER_INIT_PATH.exists() && DOCKER_SOCKET_FILE.exists();
    }

}
