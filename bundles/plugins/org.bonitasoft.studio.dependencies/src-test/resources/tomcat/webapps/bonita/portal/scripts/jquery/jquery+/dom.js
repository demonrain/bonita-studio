/*
 * Copyright (c) 2011, Séverin MOUSSEL
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of jQuery+ nor the names of its contributors may be
 *       used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * A set of DOM related tools
 */
$(function() {

	var juid = 0;

	/**
	 * Generate a unique Dom element ID
	 * 
	 * @return (String) This function generate an ID that doesn't already exists
	 *         in the whole DOM
	 */
	$.getUniqueId = function() {
		juid = juid + 1;
		while ($('#juid_' + String(juid)).length > 0) {
			juid = juid + 1;
		}
		return 'juid_' + String(juid);
	};

	/**
	 * Change IDs of an area to ensure they don't interfer with the rest of the
	 * DOM
	 * 
	 * @return (jQuery) This function returns <this> to allow cascading calls
	 */
	$.fn.makeIdsUnique = function() {
		var context = this;
		$('[id]', context).each(
				function() {
					if ($('#' + this.id).length > 1) {
						var e = $(this),
							newId = $.getUniqueId(),
							oldId = e.attr('id');

						$('[for=' + oldId + ']', e.closest('form', context)).attr('for', newId);
						e.attr('id', newId);
					}
				});
		return this;
	};
});