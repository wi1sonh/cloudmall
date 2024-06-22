"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Array) {
  const _easycom_uni_popup2 = common_vendor.resolveComponent("uni-popup");
  _easycom_uni_popup2();
}
const _easycom_uni_popup = () => "../../node-modules/@dcloudio/uni-ui/lib/uni-popup/uni-popup.js";
if (!Math) {
  _easycom_uni_popup();
}
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent({
  __name: "pushMsg",
  setup(__props, { expose: __expose }) {
    const popup = common_vendor.ref();
    const openPopup = () => {
      if (popup.value) {
        popup.value.open();
      }
    };
    const closePopup = () => {
      if (popup.value) {
        popup.value.close();
      }
    };
    const confirm = () => {
      closePopup();
    };
    __expose({
      openPopup,
      closePopup
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(confirm),
        b: common_vendor.sr(popup, "6f501330-0", {
          "k": "popup"
        }),
        c: common_vendor.p({
          type: "center",
          ["is-mask-click"]: true
        })
      };
    };
  }
});
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__file", "C:/Users/reverie/Desktop/course/软件工程/cloudmall/cloudmall-uniapp/src/components/message/pushMsg.vue"]]);
wx.createComponent(Component);
