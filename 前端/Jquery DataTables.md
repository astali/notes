Jquery DataTables的使用

```
   $('.table').dataTable({
        serverSide: true,//开启服务器模式 分页，取数据等等的都放到服务端去. true为后台分页，每次点击分页时会请求后台数据，false为前台分页
        "processing": true,//加载显示提示
        buttons: [{
        extend: 'excelHtml5',
        text:'导出', //导出当页，除非serverSide：false
        customize: function( xlsx ) {
            var sheet = xlsx.xl.worksheets['sheet1.xml'];
            $('row c[r^="C"]', sheet).attr( 's', '2' );
        }
        }],
        dom: 'lfBtip',
        "lengthMenu": [ [10, 25, 50, -1], [10, 25, 50, "所有"] ],
        "ajax": {
            url:"${pageContext.request.contextPath}/dmplog/logList",
            type: 'GET',
            data:{},
        },
        "columns": [
            { "data": null,
                render : function(data,type, row, meta) {
                    return "<input name=\"checkbox\" value=\""+row.id+"\" type=\"checkbox\" value=\"\">";
                }
            },
            { "data": "id"},
            { "data": "useraccountid",
                render: function(data,type, row, meta){
                    return "<u style=\"cursor:pointer\" class=\"text-primary\" onclick=\"member_show('用户详情','member-show','360','400')\">"+data+"</a>";
                }
            },
            { "data": "gameid"},
            { "data": "bnormalcancel" },
                /*,
                render : function(data,type, row, meta) {
                    if(data==0){
                        return "<span class=\"label label-defant radius td-status\">已停用</span>";
                    }else if(data==1){
                        return "<span class=\"label label-success radius td-status\">已启用</span>";
                    }else{
                        return "<span class=\"label label-warning radius td-status\">其它态</span>";
                    }
                }*/
            { "data": "clientip"},
            { "data": "description"},
            { "data": "remark"},
            { "data": "createtime",
                render : function(data,type, row, meta) {
                    return date(data);
                }
            },
            { "data": null,
                render : function(data,type, row, meta) {
                    if(row.state==1){
                        return "";
                    } else{
                        return "";
                    }
                }
            }
        ],
        "aaSorting": [[ 8, "asc" ]],//默认第几个排序
        "bStateSave": false,//状态保存
        "aoColumnDefs": [
            //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
            {"orderable":false,"aTargets":[0,2,3,4,5,6,7,9]}// 制定列不参与排序
        ],
        language: {
          "sProcessing":   "&nbsp;&nbsp;&nbsp;处理中...",
          "sLengthMenu":   "显示 _MENU_ 条",
          "sZeroRecords":  "没有找到匹配的记录",
          "sInfo":         "显示 _START_ 到 _END_ ，共 _TOTAL_ 条",
          "sInfoEmpty":    "没有数据",
          "sInfoFiltered": "(从 _MAX_ 条中过滤)",
          "sInfoPostFix":  "",
          "sSearch":       "从所有数据中检索：",
          "sUrl":          "",
          "sEmptyTable":     "没有数据",
          "sLoadingRecords": "载入中...",
          "sInfoThousands":  ",",
          "sDom": "T<'clear'>lfrtip",
          "oPaginate": {
            "sFirst":    "首页",
            "sPrevious": "上一页",
            "sNext":     "下一页",
            "sLast":     "末页"
          },
          "oAria": {
            "sSortAscending":  ": 以升序排列此列",
            "sSortDescending": ": 以降序排列此列"
          }
        },
        colReorder: true
    });
});
```

