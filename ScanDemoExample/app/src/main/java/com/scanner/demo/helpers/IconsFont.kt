/*
 * Copyright 2019 Mike Penz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scanner.demo.helpers

import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.ITypeface
import com.scanner.demo.R
import java.util.LinkedList

@Suppress("EnumEntryName")
object IconsFont : ITypeface {

    override val fontRes: Int
        get() = R.font.iconfont_font_v1_0_0

    override val characters: Map<String, Char> by lazy {
        Icon.values().associate { it.name to it.character }
    }
    
    override val mappingPrefix: String
        get() = "gmd"

    override val fontName: String
        get() = "IconsFont"

    override val version: String
        get() = "1.0.0"

    override val iconCount: Int
        get() = characters.size

    override val icons: List<String>
        get() = characters.keys.toCollection(LinkedList())

    override val author: String
        get() = "raj"

    override val url: String
        get() = ""

    override val description: String
        get() = ""

    override val license: String
        get() = ""

    override val licenseUrl: String
        get() = ""

    override fun getIcon(key: String): IIcon = Icon.valueOf(key)

    enum class Icon constructor(override val character: Char) : IIcon {
        gmd_add_circle_outline('\u0062'),
		gmd_arrow_back_ios('\u0063'),
		gmd_cancel('\u006a'),
		gmd_clear('\u0069'),
		gmd_close('\u0069'),
		gmd_comments('\u0071'),
		gmd_contact_mail('\u0067'),
		gmd_document_file_pdf('\u0061'),
		gmd_file_download('\u006b'),
		gmd_folder('\u0066'),
		gmd_folder_special('\u0065'),
		gmd_heart('\u006e'),
		gmd_heart_o('\u006d'),
		gmd_keyboard_backspace('\u0064'),
		gmd_pencil('\u0072'),
		gmd_send('\u0068'),
		gmd_share('\u006c'),
		gmd_thumbs_o_down('\u006f'),
		gmd_thumbs_o_up('\u0070'),
		gmd_trash('\u0073');

        override val typeface: ITypeface by lazy { IconsFont }
    }
}