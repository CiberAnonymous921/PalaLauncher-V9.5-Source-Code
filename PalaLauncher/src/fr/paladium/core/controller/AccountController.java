package fr.paladium.core.controller;

import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
import fr.paladium.core.authentication.paladium.response.PaladiumAccount;
import fr.paladium.core.utils.desktop.NotificationHelper;
import fr.paladium.core.utils.net.Telemetry;
import fr.paladium.core.utils.session.SessionStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountController {
    private static AccountController instance;
    private MinecraftAccount currentAccount;
    private Map<MinecraftAccount, PaladiumAccount> paladiumAccountMap;

    public MinecraftAccount getCurrentAccount() {
        return this.currentAccount;
    }

    public Map<MinecraftAccount, PaladiumAccount> getPaladiumAccountMap() {
        return this.paladiumAccountMap;
    }

    private AccountController() {
        instance = this;

        this.paladiumAccountMap = new HashMap<>();
        if (!SessionStorage.getInstance().hasItem("currentAccount")) {
            return;
        }

        this.currentAccount = (MinecraftAccount) SessionStorage.getInstance().getItem("currentAccount", MinecraftAccount.class);
        if (this.currentAccount != null) {
            try {
                this.currentAccount = AuthenticationController.getInstance().verify(this.currentAccount);
            } catch (Exception e) {
                e.printStackTrace();
                NotificationHelper.sendNotification("Impossible de vérifier votre compte minecraft, veuillez vous reconnecter.");
                this.currentAccount = null;
            }
        }

        saveCurrentAccount();
    }

    public List<MinecraftAccount> getAccounts() {
        if (!SessionStorage.getInstance().hasItem("accountStorage")) {
            return new ArrayList<>();
        }

        MinecraftAccountStorage storage = (MinecraftAccountStorage) SessionStorage.getInstance().getItem("accountStorage", MinecraftAccountStorage.class);
        return storage.getAccounts();
    }

    public void addAccount(MinecraftAccount account) {
        List<MinecraftAccount> accounts = getAccounts();
        accounts.add(account);
        Telemetry.collect(Telemetry.Type.INFO_ADD_ACCOUNT);
        SessionStorage.getInstance().setItem("accountStorage", new MinecraftAccountStorage(accounts));
    }

    public void removeAccount(MinecraftAccount account) {
        List<MinecraftAccount> accounts = getAccounts();
        accounts.remove(account);
        SessionStorage.getInstance().setItem("accountStorage", new MinecraftAccountStorage(accounts));
    }

    public void saveCurrentAccount() {
        List<MinecraftAccount> accounts = getAccounts();
        if (this.currentAccount != null && accounts.contains(this.currentAccount)) {
            accounts.set(accounts.indexOf(this.currentAccount), this.currentAccount);
        }
        SessionStorage.getInstance().setItem("accountStorage", new MinecraftAccountStorage(accounts));
    }

    public void saveAccount(MinecraftAccount account) {
        List<MinecraftAccount> accounts = getAccounts();
        if (account != null && accounts.contains(account)) {
            accounts.set(accounts.indexOf(account), account);
        }
        SessionStorage.getInstance().setItem("accountStorage", new MinecraftAccountStorage(accounts));
    }

    public void setCurrentAccount(MinecraftAccount account) {
        this.currentAccount = account;
        SessionStorage.getInstance().setItem("currentAccount", account);

        if (account == null) {
            return;
        }

        if (!getAccounts().contains(account)) {
            addAccount(account);
        }
    }

    public PaladiumAccount getCurrentPaladiumAccount() {
        if (this.currentAccount == null) {
            return null;
        }

        if (this.paladiumAccountMap.containsKey(this.currentAccount)) {
            return this.paladiumAccountMap.get(this.currentAccount);
        }
        return null;
    }

    public static AccountController getInstance() {
        if (instance == null) {
            new AccountController();
        }
        return instance;
    }

    private static class MinecraftAccountStorage {
        private List<MinecraftAccount> accounts;

        public MinecraftAccountStorage(List<MinecraftAccount> accounts) {
            this.accounts = accounts;
        }

        public List<MinecraftAccount> getAccounts() {
            return this.accounts;
        }
    }
}
