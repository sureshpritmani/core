package org.jboss.as.console.client.domain.model;

/**
 * @author Heiko Braun
 * @date 2/15/11
 */
public interface SubsystemStore {
    SubsystemRecord[] loadSubsystems(String profileName);
}
