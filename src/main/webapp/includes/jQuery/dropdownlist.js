/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
(function( $ ){
    $.dropdownlist = function(combodependente, j) {
        var options = '';
        options += '<option value="' + '' + '">' + 'Selecione ......' + '</option>';
        for (var i = 0; i < j.length; i++) {
            options += '<option value="' + j[i].optionValue + '">' + j[i].optionLabel + '</option>';
        }
        $(combodependente).html(options).focus();
    }

})( jQuery );

    