package com.singh.getting.sftp;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Slf4j
public class SFTPFile {
    private static final String REMOTE_HOST = "xxx"; // replace your remote host here
    private static final String USERNAME = "xxx"; // replace your username here
    private static final String PASSWORD = "xxx"; // replace your password here
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;

    private static Session setUpSFTP() throws JSchException {
        JSch jsch = new JSch();
        Session jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
        // authenticate using password
        jschSession.setPassword(PASSWORD);
        jschSession.setConfig("StrictHostKeyChecking", "no");
        // 10 second session timeout
        jschSession.connect(SESSION_TIMEOUT);

        return jschSession;
    }

    public static Boolean upload(String fileName, ByteArrayOutputStream outputStream) {
        Session jschSession = null;

        try {
            jschSession = setUpSFTP();
            Channel sftp = jschSession.openChannel("sftp");
            // 5 second timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;

            String remoteFile = "/home/sftp/in/"; // replace your path here
            // transfer file from local to remote server
            channelSftp.put(new ByteArrayInputStream(outputStream.toByteArray()), remoteFile + fileName);
            channelSftp.exit();

            return true;
        } catch (Exception e) {
            log.error("SFTP is error: ", e);
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

        return false;
    }

    public static ByteArrayOutputStream download(String filePath, String fileName) {
        Session jschSession = null;

        try {
            jschSession = setUpSFTP();
            Channel sftp = jschSession.openChannel("sftp");
            // 5 second timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            // transfer file from local to remote server
            InputStream input = channelSftp.get(filePath + fileName);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(input, output);

            channelSftp.exit();

            return output;
        } catch (Exception e) {
            log.error("SFTP is error: ", e);
        } finally {
            if(jschSession != null) {
                jschSession.disconnect();
            }
        }

        return null;
    }


}
