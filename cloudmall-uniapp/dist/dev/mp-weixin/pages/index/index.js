"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent({
  __name: "index",
  setup(__props) {
    const toOrderPage = () => {
      common_vendor.index.navigateTo({
        url: "/pages/order/order"
      });
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(toOrderPage)
      };
    };
  }
});
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-83a5a03c"], ["__file", "C:/Users/reverie/Desktop/course/软件工程/cloudmall/cloudmall-uniapp/src/pages/index/index.vue"]]);
wx.createPage(MiniProgramPage);
