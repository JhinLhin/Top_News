<template>
  <div class="seeDetails">
    <div>
      <h4>{{ detailList.title }}</h4>
    </div>
    <div style="margin-right: 250px">
      <span>{{ detailList.typeName }}</span>
      <span>{{ detailList.pageViews }}views</span>
      <span>{{ detailList.pastHours }}hours ago</span>
    </div>

    <div style="width: 500px; margin: 20px 0px 0px 70px">
      <p>
        {{ detailList.article }}
      </p>
    </div>
  </div>
</template>

<script >
import { defineComponent } from "vue";
export default defineComponent({
  name: "Detail",
});
</script>
<script  setup>
import { getshowHeadlineDetail } from "../../api/index";
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
const route = useRoute(); // 路由信息对象

const detailList = ref({}); //详情数据
//获取详情初始化数据
const getDetailList = async () => {
  let result = await getshowHeadlineDetail(route.query.hid);
  detailList.value = result.headline;
};
// 页面初始化钩子
onMounted(() => {
  getDetailList();
});
</script>

<style lang="less" scoped>
.seeDetails {
  // width: 600px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: rgb(251, 242, 242);
  opacity: 0.7;
  div {
    span {
      padding-right: 15px;
      font-size: 14px;
      color: #8d91aa;
    }
    p {
      font-size: 14px;
      color: #2b2e30;
    }
  }
}
</style>
