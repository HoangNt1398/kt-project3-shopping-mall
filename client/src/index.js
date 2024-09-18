import React from "react";
import ReactDOM from "react-dom"; // Thay đổi từ 'react-dom/client' thành 'react-dom'
import App from "./App";
import { legacy_createStore as createStore, combineReducers } from "redux";
import { Provider } from "react-redux";
import rootReducer from "./Redux/index.js";
import { configureStore } from '@reduxjs/toolkit';

import { persistStore, persistReducer } from "redux-persist";
import { PersistGate } from "redux-persist/integration/react";
import storage from "redux-persist/lib/storage";

const persistConfig = {
  key: "root",
  storage,
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
  devTools: process.env.NODE_ENV !== 'production',
  middleware: (getDefaultMiddleware) => getDefaultMiddleware({
    serializableCheck: false,
  }),
});

const persistor = persistStore(store); // redux store 생성

ReactDOM.render( // Thay đổi từ createRoot sang render
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <App />
    </PersistGate>
  </Provider>,
  document.getElementById("root") // Đối số thứ 2 được thêm vào đây
);
