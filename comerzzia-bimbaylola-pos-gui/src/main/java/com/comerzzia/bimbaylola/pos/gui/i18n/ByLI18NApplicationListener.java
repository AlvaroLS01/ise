package com.comerzzia.bimbaylola.pos.gui.i18n;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.pos.gui.i18n.I18NApplicationListener;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.i18n.I18N;

@Component
@Primary
public class ByLI18NApplicationListener extends I18NApplicationListener {

	@Override
	public void onBeforeApplicationInit() {
		super.onBeforeApplicationInit();
		I18N.addTranslationPropertiesBaseName("skins." + AppConfig.skin + ".com.comerzzia.pos.services.i18n.cmz-pos-services");
		I18N.addTranslationPropertiesBaseName("skins." + AppConfig.skin + ".com.comerzzia.pos.devices.i18n.cmz-pos-devices");
	}

	@Override
	public void onAfterApplicationInit() {

	}

	@Override
	public void onBeforeApplicationStart() {

	}

	@Override
	public void onAfterApplicationStart() {
	}

	@Override
	public void onBeforeApplicationClose() {
	}

}
