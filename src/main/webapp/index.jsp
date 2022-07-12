<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<!DOCTYPE html>
<html>
<head>
    <title>Cheesy Path</title>

    <script type='text/javascript' src='<c:url value='/resources/js/jquery.min.js' />'></script>
    <script type='text/javascript' src='<c:url value='/resources/js/jquery-ui.min.js' />'></script>
    <script type='text/javascript' src='<c:url value='/resources/js/FileSaver.js' />'></script>
    <script type='text/javascript' src='<c:url value='/resources/js/script.js' />'></script>

    <link rel='shortcut icon' href='<c:url value='/resources/img/favicon32.png' />'/>
    <link rel='stylesheet' href='<c:url value='/resources/css/Roboto.css' />'>
    <link rel='stylesheet' href='<c:url value='/resources/css/SourceCodePro.css' />'>
    <link rel='stylesheet' href='<c:url value='/resources/css/style.css' />'>
</head>
<body onload='init()'>
<input id='title' placeholder='Title'>
<div id='canvases'>
    <canvas id='background'></canvas>
    <canvas id='field' onmousedown="canvasMouseDown(this, event)" onmouseup="canvasMouseUp(this, event)"></canvas>
</div>
<div class='buttonContainer'>
    <button onclick='addPoint()'>Add Point</button>
    <button onclick='draw(3)'>Animate</button>
    <button onclick='flipField()'>Flip Field</button>
    <button onclick='update()'>Update</button>
    <span class="floatright">
        <button onclick='downloadConfig()'>Download Config</button>
        <button onclick='uploadConfig()'>Upload Config</button>
    </span>
    <br>
    <br>
    <div class="hiddenfile">
        <input type="file" id="configFileUpload">
    </div>
</div>

<table>
    <thead>
    <tr>
        <th>Path ID</th>
        <th>Max Vel</th>
        <th>Max Accel</th>
        <th>Max Voltage</th>
        <th>Max Centripetal Accel</th>
        <th>Is Reversed?</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><input type='text' value='0' maxlength="3" id='pathID'></td>
        <td><input type='text' value='-1' maxlength="3" id='maxVel'></td>
        <td><input type='text' value='-1' maxlength="3" id='maxAccel'></td>
        <td><input type='text' value='-1' maxlength="3" id='maxVoltage'></td>
        <td><input type='text' value='-1' maxlength="3" id='maxCentripetalAccel'></td>
        <td><input type='checkbox' id='isReversed'></td>
    </tr>
    </tbody>
</table>


<table>
    <thead>
    <th></th>
    <th>X</th>
    <th>Y</th>
    <th>Heading</th>
    <th>Comments</th>
    <th>Enabled</th>
    <th>Delete</th>
    </thead>
    <tbody id="pointtable">
    <tr>
        <td class='drag-handler'></td>
        <td class='x'><input type='number' value='0'></td>
        <td class='y'><input type='number' value='0'></td>
        <td class='heading'><input type='number' value='0'></td>
        <td class='comments'><input type='search' placeholder='Comments'></td>
        <td class='enabled'><input type='checkbox' checked></td>
        <td class='delete'>
            <button onclick='$(this).parent().parent().remove();update()'>&times;</button>
        </td>
    </tr>
    </tbody>
</table>

<input type='file' id='upl' style='display:none;'>
</body>
</html>

<script>
    $('table tbody').sortable({
        helper: fixWidthHelper,
        deactivate: update
    }).disableSelection();

    function fixWidthHelper(e, ui) {
        ui.children().each(function () {
            $(this).width($(this).width());
        });
        return ui;
    }
</script>
