/**
 *
 * Created by qin on 2015/3/9.
 */
var socket = io();

$(document).ready(function () {
    $("#video").click(function () {
        socket.emit('click', "video");
    });
    $("#picture").click(function () {
        socket.emit('click', "picture");
    });
    $("#file").click(function () {
        socket.emit('click', "file");
    });
    $("#message").click(function () {
        socket.emit('click', "message");
    });

    $("#myVideo").on('play', function(){
        socket.emit("action_video", "play");
    });

    $("#myVideo").on('pause', function(){
        socket.emit("action_video", "pause");
    });

    var $el = $( '#bl-main' ),
        $sections = $el.children( 'section' ),
    // works section
        $sectionWork = $( '#section_picture' ),
    // work items
        $workItems = $( '#bl-work-items > li' ),
    // work panels
        $workPanelsContainer = $( '#bl-panel-work-items' ),
        $workPanels = $workPanelsContainer.children( 'div' ),
        totalWorkPanels = $workPanels.length,
    // navigating the work panels
        $nextWorkItem = $workPanelsContainer.find( 'nav > span.bl-next-work' ),
    // if currently navigating the work items
        isAnimating = false,
    // close work panel trigger
        $closeWorkItem = $workPanelsContainer.find( 'nav > span.bl-icon-close' ),
        transEndEventNames = {
            'WebkitTransition' : 'webkitTransitionEnd',
            'MozTransition' : 'transitionend',
            'OTransition' : 'oTransitionEnd',
            'msTransition' : 'MSTransitionEnd',
            'transition' : 'transitionend'
        },
    // transition end event name
        transEndEventName = transEndEventNames[ Modernizr.prefixed( 'transition' ) ],
    // support css transitions
        supportTransitions = Modernizr.csstransitions;

    socket.on('res_click', function (msg) {
        switch (msg) {
            case 'video':
                if(!$("#section_video").data('open')) {
                    $("#section_video").data('open', true).addClass('bl-expand bl-expand-top');
                    $el.addClass('bl-expand-item');
                }
                $("#section_video").find('span.bl-icon-close').on('click', function () {
                    socket.emit('close', 'video');
                    return false;
                });
                break;
            case 'picture':
                if(!$("#section_picture").data('open')) {
                    $("#section_picture").data('open', true).addClass('bl-expand bl-expand-top');
                    $el.addClass('bl-expand-item');
                }
                $("#section_picture").find('span.bl-icon-close').on('click', function () {
                    socket.emit('close', 'picture');
                    return false;
                });
                break;
            case 'file':
                if(!$("#section_file").data('open')) {
                    $("#section_file").data('open', true).addClass('bl-expand bl-expand-top');
                    $el.addClass('bl-expand-item');
                }
                $("#section_file").find('span.bl-icon-close').on('click', function () {
                    socket.emit('close', 'file');
                    return false;
                });
                break;
            case 'message':
                if(!$("#section_message").data('open')) {
                    $("#section_message").data('open', true).addClass('bl-expand bl-expand-top');
                    $el.addClass('bl-expand-item');
                }
                $("#section_message").find('span.bl-icon-close').on('click', function () {
                    socket.emit('close', 'message');
                    return false;
                });
                break;
        }
    });

    socket.on('res_close', function (msg) {
        switch (msg) {
            case 'video':
                $("#section_video").data('open', false).removeClass('bl-expand').on(transEndEventName, function (event) {
                    if (!$(event.target).is('section')) return false;
                    $(this).off(transEndEventName).removeClass('bl-expand-top');
                });
                if (!supportTransitions) {
                    $("#section_video").removeClass('bl-expand-top');
                }
                $el.removeClass('bl-expand-item');
                break;
            case 'picture':
                $("#section_picture").data('open', false).removeClass('bl-expand').on(transEndEventName, function (event) {
                    if (!$(event.target).is('section')) return false;
                    $(this).off(transEndEventName).removeClass('bl-expand-top');
                });
                if (!supportTransitions) {
                    $("#section_picture").removeClass('bl-expand-top');
                }
                $el.removeClass('bl-expand-item');
                break;
            case 'file':
                $("#section_file").data('open', false).removeClass('bl-expand').on(transEndEventName, function (event) {
                    if (!$(event.target).is('section')) return false;
                    $(this).off(transEndEventName).removeClass('bl-expand-top');
                });
                if (!supportTransitions) {
                    $("#section_file").removeClass('bl-expand-top');
                }
                $el.removeClass('bl-expand-item');
            break;
            case 'message':
                $("#section_message").data('open', false).removeClass('bl-expand').on(transEndEventName, function (event) {
                    if (!$(event.target).is('section')) return false;
                    $(this).off(transEndEventName).removeClass('bl-expand-top');
                });
                if (!supportTransitions) {
                    $("#section_message").removeClass('bl-expand-top');
                }
                $el.removeClass('bl-expand-item');
                break;
        }
    });

    socket.on('res_video', function (msg) {
        switch (msg) {
            case 'play':
                $("#myVideo").get(0).play();
                break;
            case 'pause':
                $("#myVideo").get(0).pause();
                break;
        }
    });

    socket.on("res_select", function (msg) {
        console.log(msg);
        switch (msg) {
            case 0:
                // scale down main section
                $sectionWork.addClass( 'bl-scale-down' );

                // show panel for this work item
                $workPanelsContainer.addClass( 'bl-panel-items-show' );

                var $panel = $workPanelsContainer.find("[data-panel='" + $("#first").data( 'panel' ) + "']");
                currentWorkPanel = $panel.index();
                $panel.addClass( 'bl-show-work' );
                break;
            case 1:
                // scale down main section
                $sectionWork.addClass( 'bl-scale-down' );

                // show panel for this work item
                $workPanelsContainer.addClass( 'bl-panel-items-show' );

                var $panel = $workPanelsContainer.find("[data-panel='" + $("#second").data( 'panel' ) + "']");
                currentWorkPanel = $panel.index();
                $panel.addClass( 'bl-show-work' );
                break;
            case 2:
                // scale down main section
                $sectionWork.addClass( 'bl-scale-down' );

                // show panel for this work item
                $workPanelsContainer.addClass( 'bl-panel-items-show' );

                var $panel = $workPanelsContainer.find("[data-panel='" + $("#third").data( 'panel' ) + "']");
                currentWorkPanel = $panel.index();
                $panel.addClass( 'bl-show-work' );
                break;
            case 3:
                // scale down main section
                $sectionWork.addClass( 'bl-scale-down' );

                // show panel for this work item
                $workPanelsContainer.addClass( 'bl-panel-items-show' );

                var $panel = $workPanelsContainer.find("[data-panel='" + $("#forth").data( 'panel' ) + "']");
                currentWorkPanel = $panel.index();
                $panel.addClass( 'bl-show-work' );
                break;
        }
    });

    socket.on("res_next", function (msg) {
            currentWorkPanel = msg;
            if( isAnimating ) {
                return false;
            }
            isAnimating = true;

            var $currentPanel = $workPanels.eq( currentWorkPanel );
            currentWorkPanel = currentWorkPanel < totalWorkPanels - 1 ? currentWorkPanel + 1 : 0;
            var $nextPanel = $workPanels.eq( currentWorkPanel );

            $currentPanel.removeClass( 'bl-show-work' ).addClass( 'bl-hide-current-work' ).on( transEndEventName, function( event ) {
                if( !$( event.target ).is( 'div' ) ) return false;
                $( this ).off( transEndEventName ).removeClass( 'bl-hide-current-work' );
                isAnimating = false;
            } );

            if( !supportTransitions ) {
                $currentPanel.removeClass( 'bl-hide-current-work' );
                isAnimating = false;
            }

            $nextPanel.addClass( 'bl-show-work' );

            socket.on("action_next", currentWorkPanel);
    });

    socket.on("res_close", function (msg) {
        currentWorkPanel = msg;
        $sectionWork.removeClass( 'bl-scale-down' );
        $workPanelsContainer.removeClass( 'bl-panel-items-show' );
        $workPanels.eq( currentWorkPanel ).removeClass( 'bl-show-work' );
    });

    socket.on("res_message", function(msg){
        var ul = document.getElementById("list");
        var li = document.createElement("li");
        li.appendChild(document.createTextNode(msg));
        ul.appendChild(li);
        $("#myMessage").val('');
    });

    // Sends a chat message
    function sendMessage () {
        var message = $("#myMessage").val();
        socket.emit("message", message);
    }


    $("button").click(function(){
        sendMessage();
    });


});





