/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import model.PCStructureComponent;
import view.ConfiguratorRow;

/**
 *
 * @author Rafa
 */
public interface ConfiguratorRowListener {
    public void setProductFor(PCStructureComponent component, ConfiguratorRow row);
}
