/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.advft;

import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.table.TableLayout;
import com.zomuhtech.cn.features.procs.Proc;
import com.codename1.l10n.L10NManager;
import java.util.Date;

/**
 *
 * @author Eric
 */
public class LocalizationMgrFt extends Form {

    Form form, prevForm;
    Proc proc;

    public LocalizationMgrFt(Form form) {

        this.prevForm = form;
        proc = new Proc();
        Display.getInstance().callSerially(() -> {
            createUI();
        });
    }

    public void createUI() {
        form = proc.getForm("L10N Manager", prevForm);
        form.setLayout(new TableLayout(16, 2));

        Date date = new Date();
        L10NManager l10n = L10NManager.getInstance();

        form.add("format(double)").add(l10n.format(11.12))
                .add("format(int)").add(l10n.format(24))
                .add("formatCurrency").add(l10n.formatCurrency(53.267))
                .add("formatDateLongStyle").add(l10n.formatDateLongStyle(date))
                .add("formatDateShortStyle").add(l10n.formatDateShortStyle(date))
                .add("formatDateTime").add(l10n.formatDateTime(date))
                .add("formatDateTimeMedium").add(l10n.formatDateTimeMedium(date))
                .add("formatDateTimeShort").add(l10n.formatDateTimeShort(date))
                .add("getCurrencySymbol").add(l10n.getCurrencySymbol())
                .add("getLanguage").add(l10n.getLanguage())
                .add("getLocale").add(l10n.getLocale())
                .add("isRTLLocale").add("" + l10n.isRTLLocale())
                .add("parseCurrency").add(l10n.formatCurrency(
                l10n.parseCurrency("33.77$")))
                .add("parseDouble").add(l10n.format(l10n.parseDouble("34.35")))
                .add("parseInt").add(l10n.format(l10n.parseInt("56")))
                .add("parseLong").add("" + l10n.parseLong("4444444"));

        form.show();
    }

}
