<template>
  <v-ons-page>
    <v-ons-toolbar class="home-toolbar">
      <div class="left">
        <v-ons-toolbar-button @click="$emit('toggleSetting')">
          <v-ons-icon icon="ion-navicon, material:md-menu"></v-ons-icon>
        </v-ons-toolbar-button>
      </div>
      <div class="center">{{ msg }}</div>
    </v-ons-toolbar>

    <v-ons-pull-hook
      :action="fetchNewsList"
      @changestate="pullState = $event.state"
    >
      <span v-show="pullState === 'initial'"> 下拉刷新 </span>
      <span v-show="pullState === 'preaction'"> 松开刷新 </span>
      <span v-show="pullState === 'action'"> 加载中... </span>
    </v-ons-pull-hook>

    <v-ons-list>
      <v-ons-list-item v-for="item in news" @click="$emit('open-news', item)" :key="item.link">
        <div class="center">{{ item.title }}</div>
      </v-ons-list-item>
    </v-ons-list>
  </v-ons-page>
</template>

<script>
export default {
  data() {
    return {
      msg: "炎陵新闻",
      pullState: 'initial',
      news: []
    }
  },
  methods: {
    fetchNewsList(done) {
      setTimeout(() => {
        this.news.push({
          title: 'fuck'
        })
        done()
      }, 400)
    }
  }
}
</script>
