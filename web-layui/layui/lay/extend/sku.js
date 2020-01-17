/**
 * 商品SKU模块基于layui；
 * 使用：var SKU = sku.init({ id:'sku', item: item, data:data });
 * @config item = [{"id":1,"name":"颜色","sub":[{"id":1,"name":"黑"},{"id":2,"name":"白"},{"id":3,"name":"蓝"}]},{"id":2,"name":"尺寸","sub":[{"id":4,"name":"S"},{"id":5,"name":"M"},{"id":6,"name":"L"}]}];
 * @param data = [{"ids":[{"1":"1"},{"2":"4"}],"price":0,"stock":0,"sku":0}];
 * @author Even Yin;even@1000duo.cn;
 * @weibo https://weibo.com/yinxu46;
 * @website https://www.1000duo.cn;
 * @version 1.0.0
 * **/
;layui.define(['jquery','form'],function(e){
    "use strict";var $ = layui.jquery,form=layui.form,o ={
        c : {sv:'',iv:'',tv:'',item:[],itemed:[],data:[],tableTitleId:[],tableTitle:[],tableTd:[]},
        z:function (x,y,a) {
            for(var i=0;i<a.length;i++){if( x+'_'+y == a[i] ) return 'checked';} return '';
        },
        y:function (s,d,a,h) {
            // update
            for(var i=0;i<d.length;i++){ h += '<i class="layui-icon layui-icon-delete" name="deleteValue" data-id="' + d[i].id + '"></i>' + '<input type="checkbox" data-id="'+s+'_'+d[i].id+'" title="'+d[i].value+'" lay-skin="primary" lay-filter="item"'+ this.z(s,d[i].id,a) +'>';}
            // update
            h += '<i class="layui-icon layui-icon-addition" name="addValue" data-id="' + s + '"></i>';
            return h;
        },
        x:function (d,a,h) {
            // update
            return '<div class="layui-form-item" pane><label class="layui-form-label">'+ '<i class="layui-icon layui-icon-delete" name="deleteName" data-id="' + d.id + '"></i>' + d.name+'</label><div class="layui-input-block" data-id="'+d.id+'">'+this.y(d.id,d.sub,a,'')+'</div></div>';
        },
        w:function (d,a,h) {
            // update
            h += '<i class="layui-icon layui-icon-addition" name="addName"></i>';
            for(var i=0;i<d.length;i++){ h += this.x(d[i],a,''); } return h;
        },
        st:function () {
            return '<table class="layui-table" lay-skin="" style="margin:0"><thead>'+this.sth()+'</thead><tbody>'+this.stb()+this.stf()+'</tbody></table>';
        },
        sth:function () {
            var h = '<tr>'; for(var i=0;i<this.c.tableTitle.length;i++){ h += '<th>' +this.c.tableTitle[i]+ '</th>';} h += '<th>价格</th><th>库存</th><th>货号</th></tr>'; return h;
        },
        stb:function () {
            var t =this, h = '', na = [];
            for(var i=0;i<t.c.data.length;i++){
                h += '<tr>';
                for(var s=0;s<t.c.data[i].ids.length;s++){
                    var k = '',v='';
                    for(var item in t.c.data[i].ids[s]){
                        k = item; v = t.c.data[i].ids[s][item];
                    }
                    if(!na[k] || na[k]<1){
                        na[k] = this.ai(t.c.data[i].ids[s][item],this.c.tableTd);
                        if(na[k]>0){
                            h += '<td rowspan="'+na[k]+'">';
                            h += this.gin(item,t.c.data[i].ids[s][item]);
                            h += '</td>';
                        }
                    }
                    na[k] --;
                }
                h += '<td><input class="layui-input layui-table-input" data-index="'+i+'" data-type="price" lay-filter="input"  value="'+t.c.data[i].price+'" ></td>';
                h += '<td><input class="layui-input layui-table-input" data-index="'+i+'" data-type="stock" lay-filter="input" value="'+t.c.data[i].stock+'" ></td>';
                h += '<td><input class="layui-input layui-table-input" data-index="'+i+'" data-type="sku" lay-filter="input" value="'+t.c.data[i].sku+'" ></td>';
                h += '</tr>';
            }
            return h;
        },
        stf:function () {
            if(this.c.data.length < 2) return '';
            return '<tr><th colspan="'+this.c.tableTitle.length+'" style="text-align: center">批量修改</th><th><input type="text" class="layui-input layui-table-input" lay-filter="input" data-type="sku-price" /></th><th><input type="text" class="layui-input layui-table-input" lay-filter="input" data-type="sku-stock" /></th><th><input type="text" class="layui-input layui-table-input" lay-filter="input" data-type="sku-sku" /></th></tr>';
        },
        gin:function (a,b) {
            for( var i=0;i<this.c.item.length;i++){
                if(a == this.c.item[i].id){
                    for(var x=0;x<this.c.item[i].sub.length;x++){
                        if( this.c.item[i].sub[x].id == b ) return this.c.item[i].sub[x].value;
                    }
                }
            }
            return '';
        },
        ai:function (a,b) {
            for(var i=0;i<b.length;i++){
                for(var s=0;s<b[i].length;s++){
                    if(a == b[i][s]){
                        var c = 1;
                        for(var y=i+1;y<b.length;y++){
                            c *= (b[y].length);
                        }
                        return c;
                    }
                }
            }
            return 0;
        },
        inArray:function (s,a) { if(s && a && a.length>0){ for(var i=0;i<a.length;i++){ if(s == a[i]) return true; } } return false; },
        citd:function (i,b) {
            if(b && !this.inArray(i,this.c.itemed)) { this.c.itemed.push(i);return;}
            if(!b && this.c.itemed.indexOf(i) > -1) {this.c.itemed.splice(this.c.itemed.indexOf(i),1);return;}
        },
        cd:function () {
            var it = this.c.itemed;
            var ids = this.c.tableTitleId;
            var arr = [];
            // update
            if(it.length===0) {this.c.data = []; return;}
            for(var i=0;i<ids.length;i++){
                for(var s=0;s<it.length;s++){
                    var kv = it[s].split('_');
                    if( kv[0] == ids[i] ) {
                        if(!arr[i]) arr[i] = [];
                        arr[i].push( kv[1] );
                    }
                }
            }
            this.c.tableTd = arr;
            var sarr = [[]];
            for (var i = 0; i < arr.length; i++) {
                var tarr = [];
                for (var j = 0; j < sarr.length; j++)
                    for (var k = 0; k < arr[i].length; k++)
                        tarr.push(sarr[j].concat(arr[i][k]));
                sarr = tarr;
            }
            var narr = [];
            for( var i=0;i<sarr.length;i++ ){
                var tarr = {};
                tarr['ids'] = [];
                for(var ii=0;ii<sarr[i].length;ii++){
                    var obj = {};
                    obj[ ids[ii] ] = sarr[i][ii];
                    tarr['ids'].push( obj );
                }
                var rr = this.ced(tarr['ids']);
                tarr['price'] = rr['price'];
                tarr['stock'] = rr['stock'];
                tarr['sku'] = rr['sku'];
                narr.push(tarr);
            }
            this.c.data = JSON.parse(JSON.stringify(narr));
        },
        ced:function(ids){
            var arr = [];arr['price']=0;arr['stock']=0;arr['sku']=0;
            var nd = [];
            for(var i=0;i<ids.length;i++){
                for(var d in ids[i]) nd.push(d+'_'+ids[i][d]);
            }
            var sd = [];
            for(var i=0;i<this.c.data.length;i++){
                sd[i] = [];
                ids = this.c.data[i].ids;
                for(var s=0;s<ids.length;s++){
                    for(var d in ids[s]) sd[i].push(d+'_'+ids[s][d]);
                }
                // 比对数据
                var is = true;
                for(var s=0;s<nd.length;s++){
                    if(this.inArray(nd[s],sd[i])){
                        is = is * true;
                    }else{
                        is = is * false;
                    }
                }
                if(is){
                    arr['price'] = parseFloat(this.c.data[i].price);
                    arr['stock'] = parseInt(this.c.data[i].stock);
                    arr['sku'] = this.c.data[i].sku;
                    return arr;
                }
            }
            return arr;
        },
        sod:function () {
            this.c.itemed.sort(function (v1,v2) {
                var kv1 = v1.split('_');
                var kv2 = v2.split('_');
                if(parseInt(kv1[0]) < parseInt(kv2[0])) return -1;
                if(parseInt(kv1[0]) > parseInt(kv2[0])) return 1;
                if(parseInt(kv1[1]) < parseInt(kv2[1])) return -1;
                if(parseInt(kv1[1]) > parseInt(kv2[1])) return 1;
                return 0;
            });
        },
        stt:function (d,a) {
            var t = this,d = t.c.item,a = t.c.itemed;
            t.c.tableTitleId = [];t.c.tableTitle = [];
            for(var i=0;i<d.length;i++){
                for(var ii=0;ii<d[i].sub.length;ii++){
                    if( t.inArray( d[i].id+'_'+d[i].sub[ii].id , a) && !t.inArray( d[i].id , t.c.tableTitleId )){
                        t.c.tableTitle.push(d[i].name);
                        t.c.tableTitleId.push(d[i].id);
                    }
                }
            }
            return;
        },
        listenInput:function () {
            var t = this;
            $('input[lay-filter]').on('change',function (e) {
                var v = $(this).val();
                var p = $(this).attr('data-type');
                var i = $(this).attr('data-index');
                if(p=='sku' && t.c.data[i]) t.c.data[i].sku = v;
                if(p=='price' && t.c.data[i]) t.c.data[i].price = isNaN(parseFloat(v))?0:parseFloat(v);
                if(p=='stock' && t.c.data[i]) t.c.data[i].stock = isNaN(parseInt(v))?0:parseInt(v);
                if(p=='sku-sku'){ for(var k=0;k<t.c.data.length;k++) t.c.data[k].sku = v+(k+1);}
                if(p=='sku-price'){ for(var k=0;k<t.c.data.length;k++) t.c.data[k].price = isNaN(parseFloat(v))?0:parseFloat(v);}
                if(p=='sku-stock'){ for(var k=0;k<t.c.data.length;k++) t.c.data[k].stock = isNaN(parseInt(v))?0:parseInt(v);}
                t.rl();
            })
        },
        listen:function () {
            var t = this;
            form.on('checkbox(item)', function(data){
                var id = $(data.elem).attr('data-id');
                t.citd(id,data.elem.checked);
                t.sod();
                t.stt();
                t.cd();
                t.rl();
            });
            this.listenInput();
        },
        getItem:function () {
            return this.c.item;
        },
        getData:function () {
            return this.c.data;
        },
        si:function (d) {
            for(var i=0;i<d.length;i++){
                if(d[i].ids){
                    for(var ii=0;ii<d[i].ids.length;ii++){
                        for(var k in d[i].ids[ii]){
                            if(!this.inArray(k+'_'+d[i].ids[ii][k],this.c.itemed)) this.c.itemed.push(k+'_'+d[i].ids[ii][k]);
                        }
                    }
                }
            }
        },
        f:function (c) {
            var s=new Date().getTime(),t = this;
            t.c.sv = c.id?c.id:'sku';
            t.c.iv = c.itemelem?c.itemelem:'item-'+s;
            t.c.tv = c.tableelem?c.tableelem:'table-'+s;
            t.c.item = c.item?c.item:[];
            t.c.data = c.data?JSON.parse(JSON.stringify(c.data)):[];
            t.c.itemed = [];
            if(t.c.data){t.si(t.c.data);t.sod();t.stt();t.cd();}
        },
        rl:function () {
            $('#'+this.c.tv).html(this.st());
            this.listenInput();
        },
        l:function () {
            // update
            $('#'+this.c.sv).html('<div id="'+this.c.iv+'">'+this.w(this.c.item,this.c.itemed,'')+'</div>');
            $('#'+this.c.sv).append('<div class="layui-form-item" pane><label class="layui-form-label">SKU商品</label><div class="layui-input-block" id="'+this.c.tv+'">'+this.st()+'</div></div></div>');
            form.render('checkbox');this.listen();
        },
        init:function (c) {this.f(c);this.l();return o;}
    };o.v = '1.0.0'; e('sku', o);
});